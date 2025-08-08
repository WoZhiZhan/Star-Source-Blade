package net.wzz.starsource.load;

import net.wzz.starsource.item.*;

import java.util.ArrayList;
import java.util.List;

public class LoadedSlash {
    public static List<ItemStarSourceSlashBlade> allSlashblade = new ArrayList<>();
    public static ItemStarSlashBlade itemStarSlashBlade;
    public static ItemAstralShadowSlashBlade itemAstralShadowSlashBlade;
    public static ItemShootingStarSlashBlade itemShootingStarSlashBlade;
    public static ItemDyingFireSlashBlade itemDyingFireSlashBlade;
    public static ItemSuperAstralShadowSlashBlade itemSuperAstralShadowSlashBlade;
    public static ItemStarBurstSlashBlade itemStarBurstSlashBlade;
    public static ItemRastaGroupSlashBlade itemRastaGroupSlashBlade;
    public static ItemStarDragonSlashBlade itemStarDragonSlashBlade;
    public static ItemFrimaireSlashBlade itemFrimaireSlashBlade;
    public static ItemJudgeStarSlashBlade itemJudgeStarSlashBlade;
    public static ItemAnnihilateSlashBlade itemAnnihilateSlashBlade;
    public static void registerSlashBlades() {
        itemStarSlashBlade = new ItemStarSlashBlade();
        itemAstralShadowSlashBlade = new ItemAstralShadowSlashBlade();
        itemShootingStarSlashBlade = new ItemShootingStarSlashBlade();
        itemDyingFireSlashBlade = new ItemDyingFireSlashBlade();
        itemSuperAstralShadowSlashBlade = new ItemSuperAstralShadowSlashBlade();
        itemStarBurstSlashBlade = new ItemStarBurstSlashBlade();
        itemRastaGroupSlashBlade = new ItemRastaGroupSlashBlade();
        itemStarDragonSlashBlade = new ItemStarDragonSlashBlade();
        itemFrimaireSlashBlade = new ItemFrimaireSlashBlade();
        itemJudgeStarSlashBlade = new ItemJudgeStarSlashBlade();
        itemAnnihilateSlashBlade = new ItemAnnihilateSlashBlade();
    }
}
