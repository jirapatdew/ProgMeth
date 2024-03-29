package logic;

import java.awt.Graphics2D;
import lib.DrawingUtility;

public class ItemBullet extends CollectibleObject {

	public ItemBullet(int movingDuration, int z) {
		super(50, movingDuration, z, 30);
	}

	@Override
	public void render(Graphics2D g2) {
		DrawingUtility.drawItemBullet(g2, x, y, radius, isPointerOver);
	}

	@Override
	public void collect(PlayerStatus player) {
		if(player.getCurrentGun() instanceof SpecialGun) {
			SpecialGun spGun = (SpecialGun)(player.getCurrentGun());
			spGun.setBulletQuantity(spGun.maxBullet);
		}
	}

}
