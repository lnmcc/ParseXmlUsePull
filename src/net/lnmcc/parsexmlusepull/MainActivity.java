package net.lnmcc.parsexmlusepull;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends Activity {

	public class Person {
		private int id;
		private String name;
		private int age;

		public void setId(int id) {
			this.id = id;
		}

		public void setName(String name) {
			this.name = name;
		}

		public void setAge(int age) {
			this.age = age;
		}

		public int getId() {
			return id;
		}

		public String getName() {
			return name;
		}

		public int getAge() {
			return age;
		}
		
		@Override
		public String toString() {
			return "Person: id = " + id + " name = " + name + " age = " + age + "\n";
		}
	}

	ArrayList<Person> persons = null;
	Person newPerson = null;
	TextView tv = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		tv = (TextView) findViewById(R.id.textView);
		parseXml();
	}

	private void parseXml() {
		try {
			InputStream inStream = getAssets().open("persons.xml");
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(true);
			XmlPullParser parser = factory.newPullParser();
			parser.setInput(inStream, "UTF-8");
			int eventType = parser.getEventType();
			boolean docEnd = false;
			
			while (!docEnd) {
				switch (eventType) {
				case XmlPullParser.START_DOCUMENT:
					Log.e("PersonInfo", "Document Start");
					persons = new ArrayList<Person>();
					break;
				case XmlPullParser.START_TAG:
					Log.e("PersonInfo", "Tag Start");
					String name = parser.getName();
					if (name.equalsIgnoreCase("person")) {
						newPerson = new Person();
						newPerson.setId(Integer.valueOf(parser
								.getAttributeValue(null, "id")));
					} else if (newPerson != null) {
						if (name.equalsIgnoreCase("name")) {
							newPerson.setName(parser.nextText().trim());
						} else if (name.equalsIgnoreCase("age")) {
							newPerson.setAge(Integer.valueOf(parser.nextText().trim()));
						}
					}
					break;
				case XmlPullParser.END_TAG:
					Log.e("PersonInfo", "Tag End");
					if (newPerson != null
							&& parser.getName().equalsIgnoreCase("person")) {
						persons.add(newPerson);
						newPerson = null;
					}
					break;
				case XmlPullParser.END_DOCUMENT:
					Log.e("PersonInfo", "Document End");
					docEnd = true;
					printPersons(); // 解析完成,输出所以人的信息
					break;
				default:
					break;
				}
				eventType = parser.next();
			}

		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void printPersons() {
		Iterator<Person> iter = persons.iterator();
		StringBuffer sb = new StringBuffer();
		while(iter.hasNext()) {
			String info = iter.next().toString();
			Log.e("PersonInfo", info);
			sb.append(info);
		}
		tv.setText(sb.toString());
	}
}
