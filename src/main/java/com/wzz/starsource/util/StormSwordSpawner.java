package com.wzz.starsource.util;

import mods.flammpfeil.slashblade.SlashBlade;
import mods.flammpfeil.slashblade.entity.EntityStormSwords;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.awt.*;
import java.util.Random;

public class StormSwordSpawner {
    public static void createSpiralSwordArray(Player player, Level world, int count) {
        Vec3 playerLook = player.getLookAngle();
        Vec3 playerPos = player.position().add(0, player.getEyeHeight(), 0);
        Vec3 right = playerLook.cross(new Vec3(0, 1, 0)).normalize();
        Vec3 up = right.cross(playerLook).normalize();
        double baseSpirals = 1.5; // 基础螺旋圈数
        double maxSpirals = 4.0;  // 最大螺旋圈数
        double spiralTurns = Math.min(maxSpirals, baseSpirals + count * 0.1); // 每10把剑增加1圈
        double baseRadius = 0.8;
        double maxRadiusGrowth = Math.min(count * 0.08, 2.5); // 半径增长有上限
        double baseHeight = 0.05;
        double heightIncrement = Math.min(baseHeight + count * 0.005, 0.15); // 高度增长
        double forwardDistance = 1.5 + Math.min(count * 0.1, 1.0);
        for (int i = 0; i < count; i++) {
            double progress = i / (double) count; // 0到1的进度
            double angle = progress * Math.PI * 2 * spiralTurns; // 螺旋角度
            double radius = baseRadius + progress * maxRadiusGrowth; // 递增半径
            double height = progress * count * heightIncrement; // 递增高度
            double angleNoise = (i % 3 - 1) * 0.1; // 轻微角度扰动
            double radiusNoise = (i % 5 - 2) * 0.05; // 轻微半径扰动
            Vec3 offset = right.scale(Math.cos(angle + angleNoise) * (radius + radiusNoise))
                    .add(up.scale(Math.sin(angle + angleNoise) * (radius + radiusNoise)))
                    .add(playerLook.scale(forwardDistance))
                    .add(0, height, 0);

            Vec3 spawnPos = playerPos.add(offset);
            EntityStormSwords sword = new EntityStormSwords(SlashBlade.RegistryEvents.StormSwords, world);
            sword.setOwner(player);
            sword.setDamage(10);
            sword.setPos(spawnPos.x, spawnPos.y, spawnPos.z);
            sword.setDelay(i * 1); // 更快的发射间隔
            float colorProgress = (float) progress;
            int red = (int) (75 + colorProgress * 100);   // 75->175
            int green = (int) (139 - colorProgress * 80); // 139->59
            int blue = (int) (255 - colorProgress * 100); // 255->155
            int color = (red << 16) | (green << 8) | blue;
            sword.setColor(color);
            Vec3 centerDir = playerLook.scale(0.8);
            Vec3 inwardDir = offset.normalize().scale(-0.1); // 轻微向内
            Vec3 shootDir = centerDir.add(inwardDir).normalize();
            sword.shoot(shootDir.x, shootDir.y, shootDir.z, 4.0F, 0.2F);
            world.addFreshEntity(sword);
        }
    }

    private static void createRandomStormSwordField(Player player, Level world, int count) {
        if (world.isClientSide()) return;
        Random random = new Random();
        Vec3 playerPos = player.position();
        for (int i = 0; i < count; i++) {
            // 随机角度（0到2π）
            double angle = random.nextDouble() * 6.0D * Math.PI;
            // 高斯分布的距离，让剑主要分布在中等距离
            double distance = Math.abs(random.nextGaussian() * 8.0D) + 3.0D; // 最少3格距离
            distance = Math.min(distance, 25.0D); // 最大25格距离
            // 计算基础位置
            double baseX = Math.sin(angle) * distance;
            double baseZ = Math.cos(angle) * distance;
            double baseY = Math.cos(angle * 0.5) * (distance * 0.3); // Y轴也有变化
            // 在基础位置周围添加更多随机性
            double spreadRange = 8.0D; // 扩散范围
            double finalX = playerPos.x + baseX + (random.nextDouble() - 0.5) * spreadRange;
            double finalY = playerPos.y + baseY + (random.nextDouble() - 0.5) * spreadRange * 0.5;
            double finalZ = playerPos.z + baseZ + (random.nextDouble() - 0.5) * spreadRange;
            finalY = Math.max(playerPos.y - 5, Math.min(playerPos.y + 15, finalY));
            EntityStormSwords sword = new EntityStormSwords(SlashBlade.RegistryEvents.StormSwords, world);
            sword.setOwner(player);
            float baseDamage = 8.0f;
            float distanceMultiplier = Math.max(0.5f, 2.0f - (float)(distance / 15.0));
            sword.setDamage(baseDamage * distanceMultiplier);
            sword.setPos(finalX, finalY, finalZ);
            int baseDelay = 10;
            int randomDelay = random.nextInt(100); // 0-100tick的随机延迟
            sword.setDelay(baseDelay + randomDelay);
            int[] preferredColors = {
                    0xFF4444, 0x44FF44, 0x4444FF, 0xFFFF44, 0xFF44FF, 0x44FFFF,
                    0xFF8844, 0x88FF44, 0x4488FF, 0xFF4488, 0x88FF88, 0xFF8888
            };
            int color = preferredColors[random.nextInt(preferredColors.length)];
            sword.setColor(color);
            Vec3 targetPos = playerPos.add(
                    (random.nextDouble() - 0.5) * 6.0, // 玩家周围3格范围
                    (random.nextDouble() - 0.5) * 3.0,
                    (random.nextDouble() - 0.5) * 6.0
            );
            Vec3 shootDirection = targetPos.subtract(sword.position()).normalize();
            float baseSpeed = 2.5f;
            float speedVariation = 0.5f + random.nextFloat() * 1.5f; // 0.5-2.0倍速
            float finalSpeed = baseSpeed * speedVariation;
            sword.shoot(shootDirection.x, shootDirection.y, shootDirection.z, finalSpeed, 0.8f);
            world.addFreshEntity(sword);
        }
    }

    private static void createChaoticStormSwordField(Player player, Level world, int count) {
        if (world.isClientSide()) return;
        Random random = new Random();
        Vec3 playerPos = player.position();
        for (int i = 0; i < count; i++) {
            // 完全随机的球形分布
            double phi = random.nextDouble() * 2 * Math.PI; // 水平角度
            double theta = Math.acos(1 - 2 * random.nextDouble()); // 垂直角度（球面均匀分布）
            double radius = 5.0 + random.nextDouble() * 20.0; // 5-25格半径
            double x = playerPos.x + radius * Math.sin(theta) * Math.cos(phi);
            double y = playerPos.y + radius * Math.cos(theta);
            double z = playerPos.z + radius * Math.sin(theta) * Math.sin(phi);
            x += (random.nextGaussian() * 3.0);
            y += (random.nextGaussian() * 2.0);
            z += (random.nextGaussian() * 3.0);
            y = Math.max(playerPos.y - 10, Math.min(playerPos.y + 20, y));
            EntityStormSwords sword = new EntityStormSwords(SlashBlade.RegistryEvents.StormSwords, world);
            sword.setOwner(player);
            sword.setDamage(6.0f + random.nextFloat() * 8.0f); // 6-14伤害
            sword.setPos(x, y, z);
            sword.setDelay(random.nextInt(200));
            int color = Color.HSBtoRGB(random.nextFloat(), 0.7f + random.nextFloat() * 0.3f, 0.8f + random.nextFloat() * 0.2f);
            sword.setColor(color);
            Vec3 targetArea = playerPos.add(
                    random.nextGaussian() * 8.0,
                    random.nextGaussian() * 4.0,
                    random.nextGaussian() * 8.0
            );
            Vec3 direction = targetArea.subtract(sword.position()).normalize();
            float speed = 1.5f + random.nextFloat() * 3.0f;
            sword.shoot(direction.x, direction.y, direction.z, speed, 1.2f);
            world.addFreshEntity(sword);
        }
    }

    private static void createWaveStormSwordField(Player player, Level world, int swordsPerWave) {
        if (world.isClientSide()) return;

        int totalWaves = 5;
        Random random = new Random();
        Vec3 playerPos = player.position();
        for (int wave = 0; wave < totalWaves; wave++) {
            for (int i = 0; i < swordsPerWave; i++) {
                double waveRadius = 8.0 + wave * 4.0; // 第一波8格，逐渐扩大到24格
                double angle = random.nextDouble() * 2 * Math.PI;
                double distance = waveRadius + random.nextGaussian() * 2.0;
                double x = playerPos.x + Math.sin(angle) * distance;
                double z = playerPos.z + Math.cos(angle) * distance;
                double y = playerPos.y + (random.nextDouble() - 0.5) * 8.0;
                EntityStormSwords sword = new EntityStormSwords(SlashBlade.RegistryEvents.StormSwords, world);
                sword.setOwner(player);
                sword.setDamage(10.0f - wave * 1.0f); // 第一波伤害最高
                sword.setPos(x, y, z);
                int waveDelay = wave * 40;
                int individualDelay = random.nextInt(20); // 波内随机延迟
                sword.setDelay(waveDelay + individualDelay);
                int[] waveColors = {0xFF4444, 0x44FF44, 0x4444FF, 0xFFFF44, 0xFF44FF};
                int baseColor = waveColors[wave];
                int colorVariation = random.nextInt(0x444444);
                sword.setColor(baseColor + colorVariation);
                Vec3 shootDir = playerPos.subtract(sword.position()).normalize();
                sword.shoot(shootDir.x, shootDir.y, shootDir.z, 3.5f, 0.5f);

                world.addFreshEntity(sword);
            }
        }
    }

    public static void executeStormSwordAttack(Player player, Level world, int attackType, int count) {
        switch (attackType) {
            case 2:
                createChaoticStormSwordField(player, world, count);
                break;
            case 3:
                createWaveStormSwordField(player, world, count);
                break;
            case 1:
            default:
                createRandomStormSwordField(player, world, count);
        }
    }

    private static void createBlizzardIceSwordWave(Player player, Level world, int count) {
        if (world.isClientSide()) return;
        Vec3 playerPos = player.position();
        Vec3 playerLook = player.getLookAngle();
        Random random = new Random();
        for (int i = 0; i < count; i++) {
            float progress = (float) i / (count - 1);
            int color = getWhiteToBlueGradient(progress);
            double angle = random.nextDouble() * 6.0 * Math.PI; // 更大的角度范围
            double distance = Math.abs(random.nextGaussian() * 12.0) + 8.0; // 8-32格的超大范围
            distance = Math.min(distance, 40.0); // 最大40格
            double x = playerPos.x + Math.sin(angle) * distance + (random.nextGaussian() * 8.0);
            double z = playerPos.z + Math.cos(angle) * distance + (random.nextGaussian() * 8.0);
            double y = playerPos.y + Math.cos(angle * 0.3) * (distance * 0.2) + (random.nextGaussian() * 4.0);
            y = Math.max(playerPos.y - 8, Math.min(playerPos.y + 20, y));
            EntityStormSwords sword = new EntityStormSwords(SlashBlade.RegistryEvents.StormSwords, world);
            sword.setOwner(player);
            sword.setDamage(8.0f + progress * 8.0f);
            sword.setPos(x, y, z);
            int randomDelay = random.nextInt(120); // 0-120tick随机延迟
            sword.setDelay(randomDelay);
            sword.setColor(color);
            Vec3 roughDirection = playerLook.add(
                    (random.nextDouble() - 0.5),  // 很大的随机扩散
                    (random.nextDouble() - 0.5) * 0.6,
                    (random.nextDouble() - 0.5)
            ).normalize();
            float speed = 2.5f + random.nextFloat() * 3.0f; // 随机速度2.5-5.5
            sword.shoot(roughDirection.x, roughDirection.y, roughDirection.z, speed, 0.8f);
            world.addFreshEntity(sword);
        }
    }

    private static int getWhiteToBlueGradient(float progress) {
        // progress: 0.0 = 纯白, 1.0 = 深蓝
        if (progress <= 0.5f) {
            // 前半段：白色 -> 浅蓝 (255,255,255) -> (173,216,230)
            float localProgress = progress * 2.0f;
            int red   = (int) (255 - localProgress * (255 - 173));
            int green = (int) (255 - localProgress * (255 - 216));
            int blue  = (int) (255 - localProgress * (255 - 230));
            return (red << 16) | (green << 8) | blue;
        } else {
            // 后半段：浅蓝 -> 深蓝 (173,216,230) -> (25,25,112)
            float localProgress = (progress - 0.5f) * 2.0f;
            int red   = (int) (173 - localProgress * (173 - 25));
            int green = (int) (216 - localProgress * (216 - 25));
            int blue  = (int) (230 - localProgress * (230 - 112));
            return (red << 16) | (green << 8) | blue;
        }
    }

    private static void createArcIceSwordWave(Player player, Level world, int count) {
        if (world.isClientSide()) return;
        Vec3 playerPos = player.position().add(0, player.getEyeHeight(), 0);
        Vec3 playerLook = player.getLookAngle();
        Vec3 right = playerLook.cross(new Vec3(0, 1, 0)).normalize();
        Vec3 up = right.cross(playerLook).normalize();
        double arcRadius = 4.0; // 弧形半径
        double arcAngle = Math.PI * 0.8; // 弧形角度（约144度）
        for (int i = 0; i < count; i++) {
            float progress = (float) i / (count - 1);
            double angle = -arcAngle/2 + arcAngle * progress;
            double x = Math.cos(angle) * arcRadius;
            double z = Math.sin(angle) * arcRadius;

            Vec3 spawnPos = playerPos
                    .add(playerLook.scale(-3.0))  // 后方3格
                    .add(right.scale(x))          // 弧形X偏移
                    .add(up.scale(z * 0.3))       // 轻微的Y偏移
                    .add(0, 1.0, 0);              // 高度偏移

            EntityStormSwords sword = new EntityStormSwords(SlashBlade.RegistryEvents.StormSwords, world);
            sword.setOwner(player);
            sword.setDamage(9.0f);
            sword.setPos(spawnPos.x, spawnPos.y, spawnPos.z);

            int centerIndex = count / 2;
            int distanceFromCenter = Math.abs(i - centerIndex);
            sword.setDelay(8 + (centerIndex - distanceFromCenter) * 2);
            sword.setColor(getWhiteToBlueGradient(progress));

            Vec3 convergencePoint = playerPos.add(playerLook.scale(20.0)); // 前方20格的汇聚点
            Vec3 shootDirection = convergencePoint.subtract(spawnPos).normalize();

            sword.shoot(shootDirection.x, shootDirection.y, shootDirection.z, 4.2f, 0.1f);
            world.addFreshEntity(sword);
        }
    }

    public static void executeIceSwordWave(Player player, Level world, int waveType, int count) {
        switch (waveType) {
            case 1 -> createBlizzardIceSwordWave(player, world, count);    // 矩形阵列
            case 2 -> createArcIceSwordWave(player, world, count);            // 弧形
            default -> createBlizzardIceSwordWave(player, world, count);
        }
    }
}
