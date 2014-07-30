package com.openxc.vehicle.crash.app;

import java.io.File;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Debug;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class DisplayPatientInfo extends Activity {

	
	ListView listView_patient;
Button btn_home;
	InfoAdapter adapter;
	TextView txView_nodeName, txtView_nodeValue,txtView_patient;
Context context;
	ProgressDialog progressDialog;

	static String patientName;
	static ArrayList<String> groupNamelist ;
	static ArrayList<String> groupValuelist;
	private static boolean patienttag = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context=this;
		setContentView(R.layout.patientinfo_list);
		listView_patient=(ListView)findViewById(R.id.listView_pateint);
		txtView_patient=(TextView) findViewById(R.id.headingTxt);
		btn_home=(Button)findViewById(R.id.btn_home);
		Log.d("Debug.getNativeHeapAllocatedSize() on create",
				"" + Debug.getNativeHeapAllocatedSize());
		 groupNamelist = new ArrayList<String>();
		 groupValuelist = new ArrayList<String>();
		new ParseXmlTask().execute();
	
		adapter=new InfoAdapter(this,groupNamelist,groupValuelist);
		Log.d("Debug.getNativeHeapAllocatedSize()",
				"" + Debug.getNativeHeapAllocatedSize());
		btn_home.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				System.exit(0) ;
				
			}
		});

	
	}

	

	public void getHealthDetail() {
		try {
			/*
			 * InputSource is = new InputSource(getResources().openRawResource(
			 * R.raw.bbsample)); DocumentBuilder dBuilder =
			 * DocumentBuilderFactory.newInstance() .newDocumentBuilder();
			 * Document doc = dBuilder.parse(is);
			 */

			// Get the xml file
			File sdcard = Environment.getExternalStorageDirectory();
			File file = new File(sdcard, "MedicalData/UserMedicalData.xml");

			DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();

			Document doc = dBuilder.parse(file);

			if (doc.hasChildNodes()) {

				printNote(doc.getChildNodes());

			}
			dBuilder.reset();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static void printNote(NodeList nodeList) {

		for (int count = 0; count < nodeList.getLength(); count++) {
			Node tempNode = nodeList.item(count);

			if (tempNode.getNodeType() == Node.ELEMENT_NODE) {
				if (patienttag) {
					if (tempNode.getNodeName().toString()
							.equalsIgnoreCase("given")) {
						tempNode.getNextSibling();
						patientName = tempNode.getTextContent();
					}
					if (tempNode.getNodeName().toString()
							.equalsIgnoreCase("family")) {
						patientName = patientName + " "
								+ tempNode.getTextContent();
						// Log.w(patienttag + "patientName ++", "" +
						// patientName);
						/*
						 * txtView_heading.setText("Patient Name : "+patientName)
						 * ;
						 */
						patienttag = false;
					}
				}

				if (tempNode.hasAttributes()) {

					NamedNodeMap nodeMap = tempNode.getAttributes();

					for (int i = 0; i < nodeMap.getLength(); i++) {

						Node node = nodeMap.item(i);

						groupNamelist.add(node.getNodeName().trim());
						groupValuelist.add(node.getNodeValue().trim());

					}

				}

				if (tempNode.hasChildNodes()) {
					// Log.v("childnode", "" + tempNode.getChildNodes());
					printNote(tempNode.getChildNodes());

				}

			}

		}

	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		Log.v("+++on pause++", "execut");
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		Log.v("++++Ondestroy execute", "++++++");
		
		progressDialog=null;
		txView_nodeName=null;
		txtView_nodeValue=null;
		txtView_patient=null;
		super.onDestroy();
	}

	class ParseXmlTask extends AsyncTask<String, Integer, Boolean> {
		@Override
		protected void onPreExecute() {
			// showDialog(AUTHORIZING_DIALOG);
			progressDialog = new ProgressDialog(context);
			progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

			progressDialog.setTitle("Please wait...");
			progressDialog
					.setMessage("Fetching information from Blue Button file...");
			progressDialog.setCancelable(false);
			progressDialog.show();
		}

		@Override
		protected void onPostExecute(Boolean result) {

			// Pass the result data back to the main activity
			// Parsing.this.data = result;
			//setLayoutview();
			progressDialog.dismiss();
			txtView_patient.setText(" Name : "+patientName);
			listView_patient.setAdapter(adapter);

		}

		

		@Override
		protected Boolean doInBackground(String... params) {

			// Do all your slow tasks here but dont set anything on UI
			// ALL ui activities on the main thread
			getHealthDetail();
			return true;

		}

	}


}
