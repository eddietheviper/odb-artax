package net.mostlyoriginal.game.system.planet.cells;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import net.mostlyoriginal.game.component.PlanetCell;

/**
 * @author Daan van Yperen
 */
public class SteamCellSimulator implements CellSimulator {

    @Override
    public void process(CellDecorator c, float delta) {
        float cp = MathUtils.random(0.4f, 0.9f);
        c.cell.color = Color.rgba8888(cp, cp, cp, 1f);

        if (c.cell.nextType == null) {
            if (MathUtils.random(0,100) < 1f) {
                if (MathUtils.random(0,100) > (c.mask().temperature > 100 ? 25 : 50 ) ) {
                    c.setNextType(PlanetCell.CellType.AIR);
                } else {
                    c.setNextType(PlanetCell.CellType.WATER);
                }
                return;
            }
        }

        if (swapIfSwappable(c, MathUtils.randomBoolean() ? c.getNeighbourAboveR() : c.getNeighbourAboveL())) {
            return;
        }
        if (swapIfSwappable(c, c.getNeighbourAbove())) {
            return;
        }

        if (MathUtils.randomBoolean()) {
            c.swapWithBestFlowing(c.getNeighbourLeft());
        } else {
            c.swapWithBestFlowing(c.getNeighbourRight());
        }
    }

    @Override
    public void updateMask(CellDecorator c, float delta) {
        if (c.mask().temperature < 20) {
            c.mask().temperature++;
        }
    }
    private boolean isFlows(PlanetCell neighbourLeft) {
        return neighbourLeft.type.flows();
    }

    private boolean swapIfSwappable(CellDecorator c, PlanetCell neighbourAbove) {
        if (neighbourAbove != null && neighbourAbove.type.density != null && neighbourAbove.type.isLighter(c.cell.type)) {
            c.swapWith(neighbourAbove);
            return true;
        }
        return false;
    }
}
