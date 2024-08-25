package com.moonkite.moonpets.loader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.moonkite.moonpets.config.AnimalTemplate;
import com.moonkite.moonpets.config.BackgroundTemplate;

import java.io.*;
import java.lang.reflect.Type;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

public class AnimalTemplateLoader {

    /**
     * 加载动物配置
     * @return
     */
    public static List<AnimalTemplate> loadAnimalTemplateList(){
        List<AnimalTemplate> animals = new ArrayList<>();

        URL resourceUrl = AnimalTemplateLoader.class.getResource("/config/animalTemplate.json");

        if (resourceUrl == null) {
            System.err.println("无法找到animalTemplate.json文件");
            return animals;
        }

        try (InputStream inputStream = resourceUrl.openStream();
             Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {

            Gson gson = new Gson();
            Type listType = new TypeToken<List<AnimalTemplate>>(){}.getType();
            animals = gson.fromJson(reader, listType);
            System.out.println("成功从类路径加载了 " + animals.size() + " 个动物模板");
        } catch (Exception e) {
            System.err.println("读取或解析JSON文件时出错: " + e.getMessage());
        }

        return animals;
    }

    /**
     * 加载背景配置
     * @return
     */
    public static List<BackgroundTemplate> loadBackgroundTemplates() {
        List<BackgroundTemplate> backgroundTemplateList = new ArrayList<>();
        URL resourceUrl = AnimalTemplateLoader.class.getResource("/config/background.json");

        if (resourceUrl == null) {
            System.err.println("无法找到backgrounds.json文件");
            return backgroundTemplateList;
        }

        try (InputStream inputStream = resourceUrl.openStream();
             Reader reader = new InputStreamReader(inputStream,StandardCharsets.UTF_8)) {

            Gson gson = new Gson();
            Type listType = new TypeToken<List<BackgroundTemplate>>(){}.getType();
            backgroundTemplateList = gson.fromJson(reader, listType);
            System.out.println("成功从类路径加载了 " + backgroundTemplateList.size() + " 个背景模板");
        } catch (Exception e) {
            System.err.println("读取或解析JSON文件时出错: " + e.getMessage());
        }
        return backgroundTemplateList;

    }

    public static List<AnimalTemplate> loadAnimals(String resourcePath) {
        List<AnimalTemplate> animals = new ArrayList<>();
        Path imagesPath = Paths.get(resourcePath, "images");

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(imagesPath)) {
            for (Path animalFolder : stream) {
                if (Files.isDirectory(animalFolder)) {
                    AnimalTemplate animal = createAnimalTemplate(animalFolder);
                    animals.add(animal);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return animals;
    }

    private static AnimalTemplate createAnimalTemplate(Path animalFolder) throws IOException {
        String animalName = animalFolder.getFileName().toString();
        String imagePath = animalFolder.toString();

        AnimalTemplate animal = new AnimalTemplate();
        animal.setAnimalName(animalName);
        animal.setImagePath("/images/" + animalName + "/");
        animal.setAnimalType("land"); // 设置为陆地动物

        Set<String> colorSet = new HashSet<>();
        Set<String> actionSet = new HashSet<>();

        try (DirectoryStream<Path> files = Files.newDirectoryStream(animalFolder)) {
            for (Path file : files) {
                String fileName = file.getFileName().toString();
                String tempFileName = fileName.replace("_8fps.gif", "");
                String[] parts = tempFileName.split("_");
                String color = parts[0];
                colorSet.add(color);
                if (parts.length == 2) {
                    actionSet.add(parts[1]);
                }else if(parts.length ==3) {
                    actionSet.add(parts[1] + "_" + parts[2]);
                }
            }
        }

        animal.setActionList(new ArrayList<>(actionSet));
        animal.setColorList(new ArrayList<>(colorSet));
        return animal;
    }

    public static void main(String[] args) {
//        String resourcePath = "src/main/resources"; // 根据您的项目结构调整路径
//        List<AnimalTemplate> animals = loadAnimals(resourcePath);
//
//        saveAnimalsToJson(animals, resourcePath);

        loadAnimalTemplateList();
    }

    private static void saveAnimalsToJson(List<AnimalTemplate> animals, String resourcePath) {
        // 创建Gson对象，设置美化输出
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        // 将animals列表转换为JSON字符串
        String jsonAnimals = gson.toJson(animals);

        // 确保config目录存在
        Path configPath = Paths.get(resourcePath, "config");
        try {
            Files.createDirectories(configPath);
        } catch (IOException e) {
            System.err.println("创建config目录失败: " + e.getMessage());
            return;
        }

        // 定义JSON文件路径
        File jsonFile = new File(configPath.toFile(), "animalTemplate.json");

        // 将JSON写入文件
        try (FileWriter writer = new FileWriter(jsonFile)) {
            writer.write(jsonAnimals);
            System.out.println("动物数据已保存到: " + jsonFile.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("保存JSON文件时出错: " + e.getMessage());
        }
    }
}
