package at.tugraz.ist.catroid.test.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.test.AndroidTestCase;
import android.util.Log;
import android.util.Pair;
import at.tugraz.ist.catroid.R;
import at.tugraz.ist.catroid.constructionSite.content.BrickDefine;
import at.tugraz.ist.catroid.test.utils.TestDefines;
import at.tugraz.ist.catroid.utils.parser.Parser;

public class ParserTest extends AndroidTestCase {
	private File tempFile;

	public ParserTest() {
		super();
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		if (tempFile != null && tempFile.exists())
			tempFile.delete();
		super.tearDown();
	}

	public void testParse() throws Throwable {
		Parser parser = new Parser();
		OutputStream outputStream = null;
		try {
			tempFile = File.createTempFile("project", ".xml");
			if (tempFile.canWrite()) {
				outputStream = new FileOutputStream(tempFile);
				outputStream.write(new TestDefines().getTestXml(getContext()).getBytes());
				outputStream.flush();
			}
		} catch (IOException e) {
			Log.e("ParserTest", "Writing Test XML to file failed");
			e.printStackTrace();
		} finally {
			if (outputStream != null)
				outputStream.close();
		}

		ArrayList<Pair<String, ArrayList<HashMap<String, String>>>> list = null;
		InputStream inputStream = null;
		try {
			inputStream = new FileInputStream(tempFile);
			list = parser.parse(inputStream, this.getContext());
		} catch (FileNotFoundException e) {
			Log.e("ParserTest", "Reading from test XML file failed!");
			e.printStackTrace();
		} finally {
			if(inputStream != null)
				inputStream.close();
		}
		
		// TODO test if first element is stage (name does not need to be
		// 'stage')

		// test some of the data
		assertEquals(3, list.get(0).second.size());
		assertEquals(String.valueOf(BrickDefine.SET_BACKGROUND), list.get(0).second.get(0).get(BrickDefine.BRICK_TYPE));

		assertEquals("bla.jpg", list.get(0).second.get(0).get(BrickDefine.BRICK_VALUE));
		assertEquals("0", list.get(0).second.get(0).get(BrickDefine.BRICK_ID));

		assertEquals(String.valueOf(BrickDefine.WAIT), list.get(0).second.get(1).get(BrickDefine.BRICK_TYPE));
		assertEquals("1", list.get(0).second.get(1).get(BrickDefine.BRICK_ID));
		assertEquals(String.valueOf(BrickDefine.PLAY_SOUND), list.get(0).second.get(2).get(BrickDefine.BRICK_TYPE));

		assertEquals(9, list.get(1).second.size());
		assertEquals(String.valueOf(BrickDefine.SET_COSTUME), list.get(1).second.get(0).get(BrickDefine.BRICK_TYPE));
		assertEquals("3", list.get(1).second.get(0).get(BrickDefine.BRICK_ID));
		assertEquals(String.valueOf(BrickDefine.GO_TO), list.get(1).second.get(1).get(BrickDefine.BRICK_TYPE));
		assertEquals("5", list.get(1).second.get(1).get(BrickDefine.BRICK_VALUE));
		assertEquals("7", list.get(1).second.get(1).get(BrickDefine.BRICK_VALUE_1));
		assertEquals(String.valueOf(BrickDefine.HIDE), list.get(1).second.get(2).get(BrickDefine.BRICK_TYPE));
		assertEquals(String.valueOf(BrickDefine.SHOW), list.get(1).second.get(3).get(BrickDefine.BRICK_TYPE));
		assertEquals(String.valueOf(BrickDefine.SET_COSTUME), list.get(1).second.get(4).get(BrickDefine.BRICK_TYPE));
		assertEquals("7", list.get(1).second.get(4).get(BrickDefine.BRICK_ID));

		Log.i("ParserTest", "the name of the first element: " + list.get(0).first);
		assertEquals(getContext().getString(R.string.stage), list.get(0).first);
	}

	public void testToXml() throws NameNotFoundException {
		Parser parser = new Parser();
		ArrayList<HashMap<String, String>> brickList = new ArrayList<HashMap<String, String>>();

		// create bricks for stage
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(BrickDefine.BRICK_ID, "0");
		map.put(BrickDefine.BRICK_TYPE, Integer.toString(BrickDefine.SET_BACKGROUND));
		map.put(BrickDefine.BRICK_VALUE, "bla.jpg");
		map.put(BrickDefine.BRICK_VALUE_1, "bla.jpg");
		brickList.add(map);

		map = new HashMap<String, String>();
		map.put(BrickDefine.BRICK_ID, "1");
		map.put(BrickDefine.BRICK_TYPE, Integer.toString(BrickDefine.WAIT));
		map.put(BrickDefine.BRICK_VALUE, "5");
		brickList.add(map);

		map = new HashMap<String, String>();
		map.put(BrickDefine.BRICK_ID, "2");
		map.put(BrickDefine.BRICK_TYPE, Integer.toString(BrickDefine.PLAY_SOUND));
		map.put(BrickDefine.BRICK_NAME, "bla");
		map.put(BrickDefine.BRICK_VALUE, "bla.mp3");
		brickList.add(map);

		ArrayList<Pair<String, ArrayList<HashMap<String, String>>>> spritesMap = new ArrayList<Pair<String, ArrayList<HashMap<String, String>>>>();
		spritesMap.add(new Pair<String, ArrayList<HashMap<String, String>>>(getContext().getString(R.string.stage), brickList));

		// create bricks for sprite
		brickList = new ArrayList<HashMap<String, String>>();
		map = new HashMap<String, String>();
		map.put(BrickDefine.BRICK_ID, "3");
		map.put(BrickDefine.BRICK_TYPE, Integer.toString(BrickDefine.SET_COSTUME));
		map.put(BrickDefine.BRICK_VALUE, "bla.jpg");
		map.put(BrickDefine.BRICK_VALUE_1, "bla.jpg");
		brickList.add(map);

		map = new HashMap<String, String>();
		map.put(BrickDefine.BRICK_ID, "4");
		map.put(BrickDefine.BRICK_TYPE, Integer.toString(BrickDefine.GO_TO));
		map.put(BrickDefine.BRICK_VALUE, "5");
		map.put(BrickDefine.BRICK_VALUE_1, "7");
		brickList.add(map);

		map = new HashMap<String, String>();
		map.put(BrickDefine.BRICK_ID, "5");
		map.put(BrickDefine.BRICK_TYPE, Integer.toString(BrickDefine.HIDE));
		brickList.add(map);

		map = new HashMap<String, String>();
		map.put(BrickDefine.BRICK_ID, "6");
		map.put(BrickDefine.BRICK_TYPE, Integer.toString(BrickDefine.SHOW));
		brickList.add(map);

		map = new HashMap<String, String>();
		map.put(BrickDefine.BRICK_ID, "7");
		map.put(BrickDefine.BRICK_TYPE, Integer.toString(BrickDefine.SET_COSTUME));
		map.put(BrickDefine.BRICK_VALUE, "bla.jpg");
		map.put(BrickDefine.BRICK_VALUE_1, "bla.jpg");
		brickList.add(map);

		map = new HashMap<String, String>();
		map.put(BrickDefine.BRICK_ID, "8");
		map.put(BrickDefine.BRICK_TYPE, Integer.toString(BrickDefine.SCALE_COSTUME));
		map.put(BrickDefine.BRICK_VALUE, "50");
		brickList.add(map);

		map = new HashMap<String, String>();
		map.put(BrickDefine.BRICK_ID, "9");
		map.put(BrickDefine.BRICK_TYPE, Integer.toString(BrickDefine.COME_TO_FRONT));
		brickList.add(map);

		map = new HashMap<String, String>();
		map.put(BrickDefine.BRICK_ID, "10");
		map.put(BrickDefine.BRICK_TYPE, Integer.toString(BrickDefine.GO_BACK));
		map.put(BrickDefine.BRICK_VALUE, "3");
		brickList.add(map);

		map = new HashMap<String, String>();
		map.put(BrickDefine.BRICK_ID, "11");
		map.put(BrickDefine.BRICK_TYPE, Integer.toString(BrickDefine.TOUCHED));
		brickList.add(map);

		spritesMap.add(new Pair<String, ArrayList<HashMap<String, String>>>("sprite", brickList));

		String result = parser.toXml(spritesMap, this.getContext());
		String expected = new TestDefines().getTestXml(getContext());
		Log.i("testToXml result", result);
		Log.i("testToXml expected", expected);

		assertEquals("constructed list with commands", expected, result);

		brickList.clear();
		spritesMap.clear();
		spritesMap.add(new Pair<String, ArrayList<HashMap<String, String>>>(getContext().getString(R.string.stage), brickList));

		result = parser.toXml(spritesMap, this.getContext());
		PackageInfo packageInfo = getContext().getPackageManager().getPackageInfo("at.tugraz.ist.catroid", 0);
		int expectedVersionCode = packageInfo.versionCode;
		String expectedVersionName = packageInfo.versionName;
		expected = "<?xml version='1.0' encoding='UTF-8' standalone='yes' ?>" + "<project versionCode=\"" + expectedVersionCode + "\" versionName=\""
				+ expectedVersionName + "\">" + "<stage /></project>";
		assertEquals("constructed list without commands", expected, result);

	}
	
}