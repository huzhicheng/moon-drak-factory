package com.moonkite.moonpets.config;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@State(
        name = "MoonPetsState",
        storages = {@Storage("MoonPetsPlugin.xml")}
)
public class MoonPetsState implements PersistentStateComponent<MoonPetsState> {
    public List<PetUserConfig> animals = new ArrayList<>();

    public PetUserBackgroundConfig backgroundConfig = new PetUserBackgroundConfig();

    /**
     * 是否开启打扰、冒犯模式
     */
    public boolean enableOffend;

    @Nullable
    @Override
    public MoonPetsState getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull MoonPetsState state) {
        XmlSerializerUtil.copyBean(state, this);
    }

    public void addAnimal(PetUserConfig animal) {
        animals.add(animal);
    }

    public List<PetUserConfig> getAnimals() {
        return animals;
    }

    public void clearAllAnimals() {
        if (animals != null) {
            animals.clear();
        } else {
            animals = new ArrayList<>();
        }
    }

    public void setAnimals(List<PetUserConfig> animals) {
        this.animals = animals;
    }

    public PetUserBackgroundConfig getBackgroundConfig() {
        return backgroundConfig;
    }

    public void setBackgroundConfig(PetUserBackgroundConfig backgroundConfig) {
        this.backgroundConfig = backgroundConfig;
    }

    public boolean getEnableOffend() {
        return enableOffend;
    }

    public void setEnableOffend(boolean enableOffend) {
        this.enableOffend = enableOffend;
    }
}