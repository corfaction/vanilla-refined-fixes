package net.corfaction.vanillarefined.fixes.client.ghast;

public class GhastAnimationState {

    public float currentScale = 1.0f;
    public float previousScale = 1.0f;

    public boolean wasCharging = false;

    public float recoil = 0.0f;

    public static final float NORMAL_SCALE = 1.0f;
    public static final float CHARGE_SCALE = 1.15f;
    public static final float RECOIL_SCALE = 0.90f;

    public static final float CHARGE_SPEED = 0.008f;
    public static final float RECOIL_SPEED = 0.08f;
}
