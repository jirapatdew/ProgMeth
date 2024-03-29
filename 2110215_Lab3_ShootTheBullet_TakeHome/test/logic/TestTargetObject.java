package logic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.Graphics2D;

import org.junit.Test;

public class TestTargetObject {

	@Test
	public void testRenderable(){
		TargetObject target = new TargetObject(50,50,50){
			@Override
			public void render(Graphics2D g2) {}
		};
		assertEquals(50, target.getZ());
		
		target.isDestroyed = true;
		assertFalse(target.isVisible());
		target.isDestroyed = false;
		assertTrue(target.isVisible());
	}
}
