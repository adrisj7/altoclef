package adris.altoclef.tasks.resources;


import adris.altoclef.AltoClef;
import adris.altoclef.tasks.CraftInInventoryTask;
import adris.altoclef.tasks.MineAndCollectTask;
import adris.altoclef.tasks.ResourceTask;
import adris.altoclef.tasksystem.Task;
import adris.altoclef.util.CraftingRecipe;
import adris.altoclef.util.ItemTarget;
import adris.altoclef.util.MiningRequirement;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.Items;


public class CollectNetherBricksTask extends ResourceTask {
    private final int count;

    public CollectNetherBricksTask(int count) {
        super(Items.NETHER_BRICKS, count);
        this.count = count;
    }

    @Override
    protected boolean shouldAvoidPickingUp(AltoClef mod) {
        return false;
    }

    @Override
    protected void onResourceStart(AltoClef mod) {
        mod.getBlockTracker().trackBlock(Blocks.NETHER_BRICKS);
    }

    @Override
    protected Task onResourceTick(AltoClef mod) {

        /*
         * If we find nether bricks, mine them.
         *
         * Otherwise craft them from the "nether_brick" item.
         */

        if (mod.getBlockTracker().anyFound(Blocks.NETHER_BRICKS)) {
            return new MineAndCollectTask(Items.NETHER_BRICKS, count, new Block[]{ Blocks.NETHER_BRICKS }, MiningRequirement.WOOD);
        }

        ItemTarget b = new ItemTarget("nether_brick", 1);
        return new CraftInInventoryTask(new ItemTarget(Items.NETHER_BRICK, count),
                                        CraftingRecipe.newShapedRecipe("nether_brick", new ItemTarget[]{ b, b, b, b }, 1));
    }

    @Override
    protected void onResourceStop(AltoClef mod, Task interruptTask) {
        mod.getBlockTracker().stopTracking(Blocks.NETHER_BRICKS);
    }

    @Override
    protected boolean isEqualResource(ResourceTask obj) {
        return obj instanceof CollectNetherBricksTask;
    }

    @Override
    protected String toDebugStringName() {
        return "Collecting " + count + " nether bricks.";
    }
}
