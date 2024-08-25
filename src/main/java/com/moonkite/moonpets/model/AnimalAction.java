package com.moonkite.moonpets.model;

import java.util.Random;

/**
 * @author 风筝
 * @date 2024/8/17 17:35
 */
public enum AnimalAction {

    /**
     * 动作枚举
     */
    IDEL("休闲","idle",0,0),

    LIE("躺着","lie",0,0),

    STAND("站立","stand",0,0),
    RUN("跑","run",5,0),
    SWIPE("滑动","swipe",0,0),
    WALK("走路","walk",2,0),
    WALK_FAST("快走","walk_fast",3,0),
    WITH_BALL("玩球","with_ball",0,0),

    FALL_FROMGRAB("从高处掉落","fall_fromgrab",0,3),

    JUMP("跳","jump",0,30),

    LAND("着陆","land",0,3),

    WALLCLIMB("爬墙","wallclimb",0,2),

    WALLGRAB("撞墙","wallgrab",0,10);

    private static final Random RANDOM = new Random();
    /**
     * 动作描述
     */
    AnimalAction(String name, String code, Integer speed,Integer verticalSpeed) {
        this.name = name;
        this.code = code;
        this.speed = speed;
        this.verticalSpeed = verticalSpeed;
    }

    public static AnimalAction getByCode(String code) {
        for (AnimalAction type : values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("未找到匹配的AnimalAction: " + code);
    }

    private String name;

    private String code;

    private Integer speed;

    private Integer verticalSpeed;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getSpeed() {
        return speed;
    }

    public void setSpeed(Integer speed) {
        this.speed = speed;
    }

    public Integer getVerticalSpeed() {
        return verticalSpeed;
    }

    public void setVerticalSpeed(Integer verticalSpeed) {
        this.verticalSpeed = verticalSpeed;
    }
}
