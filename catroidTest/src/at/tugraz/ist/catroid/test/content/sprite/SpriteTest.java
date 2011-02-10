/**
 *  Catroid: An on-device graphical programming language for Android devices
 *  Copyright (C) 2010  Catroid development team 
 *  (<http://code.google.com/p/catroid/wiki/Credits>)
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package at.tugraz.ist.catroid.test.content.sprite;

import java.lang.reflect.Field;

import android.test.AndroidTestCase;
import android.util.Log;
import at.tugraz.ist.catroid.content.brick.HideBrick;
import at.tugraz.ist.catroid.content.brick.ScaleCostumeBrick;
import at.tugraz.ist.catroid.content.brick.ShowBrick;
import at.tugraz.ist.catroid.content.script.Script;
import at.tugraz.ist.catroid.content.sprite.Costume;
import at.tugraz.ist.catroid.content.sprite.Sprite;

public class SpriteTest extends AndroidTestCase {
	public void testDefaultConstructor() {
		final String spriteName = "new sprite";
		Sprite sprite = new Sprite(spriteName);
		assertEquals("Unexpected Sprite name", spriteName, sprite.getName());
		assertEquals("Unexpected default x position", 0, sprite.getXPosition());
		assertEquals("Unexpected default y position", 0, sprite.getYPosition());
		assertEquals("Unexpected default z position", 0, sprite.getZPosition());
		assertEquals("Unexpected default scale", 1.0, sprite.getScale());
		assertTrue("Unexpected default visibility", sprite.isVisible());
		assertNull("Unexpected Sprite costume", sprite.getCurrentCostume());
		assertNotNull("Script list was not initialized", sprite.getScriptList());
		assertEquals("Script list contains items after constructor", 0, sprite
				.getScriptList().size());
		assertNotNull("Costume list was not initialized",
				sprite.getCostumeList());
		assertEquals("Costume list contains items after constructor", 0, sprite
				.getCostumeList().size());
	}

	public void testPositionConstructor() {
		final String spriteName = "new sprite";
		final int xPosition = 100;
		final int yPosition = -500;
		Sprite sprite = new Sprite(spriteName, xPosition, yPosition);
		assertEquals("Unexpected Sprite name", spriteName, sprite.getName());
		assertEquals("Unexpected x position", xPosition, sprite.getXPosition());
		assertEquals("Unexpected y position", yPosition, sprite.getYPosition());
		assertEquals("Unexpected default z position", 0, sprite.getZPosition());
		assertEquals("Unexpected default scale", 1.0, sprite.getScale());
		assertTrue("Unexpected default visibility", sprite.isVisible());
		assertNull("Unexpected Sprite costume", sprite.getCurrentCostume());
		assertNotNull("Script list was not initialized", sprite.getScriptList());
		assertEquals("Script list contains items after constructor", 0, sprite
				.getScriptList().size());
		assertNotNull("Costume list was not initialized",
				sprite.getCostumeList());
		assertEquals("Costume list contains items after constructor", 0, sprite
				.getCostumeList().size());

		sprite = new Sprite(spriteName, Integer.MAX_VALUE, Integer.MIN_VALUE);
		assertEquals(
				"Failed to set Sprite X position to maximum Integer value",
				Integer.MAX_VALUE, sprite.getXPosition());
		assertEquals(
				"Failed to set Sprite Y position to minimum Integer value",
				Integer.MIN_VALUE, sprite.getYPosition());
	}

	public void testSetXYPosition() {
		Sprite sprite = new Sprite("new sprite");
		final int xPosition = 5;
		final int yPosition = 8;

		sprite.setXYPosition(xPosition, yPosition);
		assertEquals("Unexpected xPosition", xPosition, sprite.getXPosition());
		assertEquals("Unexpected yPosition", yPosition, sprite.getYPosition());

		sprite.setXYPosition(Integer.MAX_VALUE, Integer.MAX_VALUE);
		assertEquals(
				"Failed to set Sprite X position to maximum Integer value",
				Integer.MAX_VALUE, sprite.getXPosition());
		assertEquals(
				"Failed to set Sprite Y position to minimum Integer value",
				Integer.MAX_VALUE, sprite.getYPosition());
	}

	public void testSetZPosition() {
		Sprite sprite = new Sprite("new sprite");
		final int zPosition = 6;

		sprite.setZPosition(zPosition);
		assertEquals("Unexpected zPosition", zPosition, sprite.getZPosition());

		sprite.setZPosition(Integer.MAX_VALUE);
		assertEquals(
				"Failed to set Sprite Z position to maximum Integer value",
				Integer.MAX_VALUE, sprite.getZPosition());

		sprite.setZPosition(-zPosition);
		assertEquals("Failed to set z position to negative value", -zPosition,
				sprite.getZPosition());
	}

	public void testScriptList() {
		Sprite sprite = new Sprite("new sprite");
		sprite.getScriptList().add(new Script());
		assertEquals("Script list does not contain script after adding", 1,
				sprite.getScriptList().size());

		sprite.getScriptList().clear();
		assertEquals("Script list could not be cleared", 0, sprite
				.getScriptList().size());
	}

	public void testCostumeList() {
		Sprite sprite = new Sprite("new sprite");
		sprite.getCostumeList().add(new Costume());
		assertEquals("Costume list does not contain costume after adding", 1,
				sprite.getCostumeList().size());

		sprite.getCostumeList().clear();
		assertEquals("Costume list could not be cleared", 0, sprite
				.getCostumeList().size());
	}

	public void testSetScale() {
		Sprite sprite = new Sprite("new sprite");
		final double scale = 2.0;
		sprite.setScale(scale);
		assertEquals("Unexpected scale", scale, sprite.getScale());

		final double hugeScale = 10.0e100;
		sprite.setScale(hugeScale);
		assertEquals("Failed to scale sprite to a very large size", hugeScale,
				sprite.getScale());

		final double tinyScale = 10.0e-100;
		sprite.setScale(tinyScale);
		assertEquals("Failed to scale sprite to a very small size", tinyScale,
				sprite.getScale());
	}

	public void testZeroScale() {
		Sprite sprite = new Sprite("testSprite");

		ScaleCostumeBrick brick = new ScaleCostumeBrick(sprite, 0.0);

		try {
			brick.execute();
			fail("Execution of ScaleCostumeBrick with 0.0 scale did not cause a IllegalArgumentException to be thrown.");
		} catch (IllegalArgumentException e) {
			// expected behavior
		}
	}

	public void testNegativeScale() {
		Sprite sprite = new Sprite("testSprite");

		final double scale = -5.0;
		ScaleCostumeBrick brick = new ScaleCostumeBrick(sprite, scale);

		try {
			brick.execute();
			fail("Execution of ScaleCostumeBrick with negative scale did not cause a IllegalArgumentException to be thrown.");
		} catch (IllegalArgumentException e) {
			// expected behavior
		}
	}

	public void testShowAndHide() {
		Sprite sprite = new Sprite("new sprite");
		assertTrue("Unexpected default visibility", sprite.isVisible());
		sprite.hide();
		assertFalse("Sprite still visible after calling hide method",
				sprite.isVisible());
		sprite.show();
		assertTrue("Sprite not visible after calling show method",
				sprite.isVisible());
	}

	public void testCurrentCostume() {
		Sprite sprite = new Sprite("new sprite");
		Costume costume = new Costume();
		sprite.getCostumeList().add(costume);
		sprite.setCurrentCostume(costume);
		assertEquals("Costume not in list after adding", costume, sprite
				.getCostumeList().get(0));
		assertEquals("Current costume was not set correctly", costume,
				sprite.getCurrentCostume());

		Costume anotherCostume = new Costume();
		try {
			sprite.setCurrentCostume(anotherCostume);
			fail("Could set current costume to a costume that's not in the list");
		} catch (IllegalArgumentException e) {
			// expected behavior
		}

		try {
			sprite.setCurrentCostume(null);
			fail("Could set current costume to null");
		} catch (IllegalArgumentException e) {
			// expected behavior
		}
	}

	public void testPauseUnPause() {
		Sprite testSprite = new Sprite("testSprite");
		Script testScript = new Script();
		HideBrick hideBrick = new HideBrick(testSprite);
		ShowBrick showBrick = new ShowBrick(testSprite);

		for (int i = 0; i < 50; i++) {
			testScript.addBrick(hideBrick);
			testScript.addBrick(showBrick);
		}

		testSprite.getScriptList().add(testScript);

		testSprite.startScripts();
		
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		testSprite.pause();
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		int brickCount = getBrickCount(testScript);
		Log.d("SpriteTest ", "Paused at brickCount  " + brickCount);
		
		testSprite.unpause();
		
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		

	}
	
	 private int getBrickCount(Script script){
	        Field field = null;
	        int brickCount = 0;
	        try {
	            field = Script.class.getDeclaredField("brickCount");
	        } catch (Exception e) {
	            e.printStackTrace();
	        } 
	        field.setAccessible(true);
	        try {
	        	brickCount = (Integer) field.get(script);
	        } catch (IllegalArgumentException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        } catch (IllegalAccessException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }
	        return brickCount;
	    }

}