package com.rss.siulp.siulp;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.provider.DocumentsContract;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by marco on 03/12/2016.
 */

public class ReadRSS extends AsyncTask<Void,Void,Void> {

    Context context;
    String address="http://www.siulpancona.it/?feed=rss2";
    ProgressDialog progress;
    ArrayList<FeedItem> feeds;
    RecyclerView recyclerView;
    Boolean ancona;
    Boolean italia;
    URL url;

    public ReadRSS(Context context, RecyclerView recyclerView, Boolean ancona, Boolean italia){
        this.ancona = ancona;
        this.italia = italia;
        this.recyclerView = recyclerView;
        this.context = context;
        feeds = new ArrayList<>();
        progress = new ProgressDialog(context);
        progress.setMessage("Caricamento dei feed...");
    }
    @Override
    protected Void doInBackground(Void... voids) {
        if(ancona) {
            ProcessXml(GetData());
        }
        if(italia) {
            address = "http://www.siulp.it/feed";
            ProcessXml(GetData());
        }

        return null;
    }

    @Override
    protected void onPreExecute() {
        progress.show();
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        progress.dismiss();
        sortList();
        MyAdapter adapter = new MyAdapter(context,feeds);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new VerticalSpace(50));
    }


    void sortList(){
        Collections.sort(feeds, new Comparator<FeedItem>() {
            @Override
            public int compare(FeedItem feedItem, FeedItem t1) {
                String format = "EEE, dd MMM yyyy HH:mm:ss";
                SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.ENGLISH);
                Date date1 =  null;
                Date date2 = null;
                String FirstDate,SecondDate;
                FirstDate = feedItem.getPubDate().toString();
                SecondDate = t1.getPubDate().toString();
                try{
                    date1 =  formatter.parse(FirstDate);
                } catch(Exception e){
                    System.out.println(e.getMessage());
                }
                try{
                    date2 =  formatter.parse(SecondDate);
                } catch(Exception e){
                    System.out.println(e.getMessage());
                }
                return date2.compareTo(date1);


            }
        });
    }

    private void ProcessXml(Document data) {
        if (data != null) {
            Element root = data.getDocumentElement();
            Node channel = root.getChildNodes().item(1);
            NodeList items = channel.getChildNodes();
            for (int i = 0; i < items.getLength(); i++) {
                Node currentChild = items.item(i);
                if (currentChild.getNodeName().equalsIgnoreCase("item")) ;
                {
                    FeedItem item = new FeedItem();
                    NodeList itemChilds = currentChild.getChildNodes();
                    for (int j = 0; j < itemChilds.getLength(); j++) {
                        Node current = itemChilds.item(j);
                        if (current.getNodeName().equalsIgnoreCase("title")) {
                            item.setTitle(current.getTextContent());
                        } else if (current.getNodeName().equalsIgnoreCase("description")) {
                            String upToNCharacters = current.getTextContent().substring(0, Math.min(current.getTextContent().length(), 200));
                            item.setDescription(upToNCharacters.replace("L'articolo", "") + "...");
                        } else if (current.getNodeName().equalsIgnoreCase("pubDate")) {
                            item.setPubDate(current.getTextContent().replace(" +0000", ""));
                        } else if (current.getNodeName().equalsIgnoreCase("link")) {
                            item.setLink(current.getTextContent());
                        }
                    }
                    if(item.getTitle()!=null && item.getDescription() != null && item.getLink() != null && item.getPubDate() != null) {
                        String url = "http://www.torinosiulp.it/images/stories/siulp_logo_nuovo.jpg";
                        item.setThumbnailUrl(url);
                        if (item.getTitle() != null)
                            feeds.add(item);
                        if (item.getTitle() != null)
                            Log.d("itemTitle", item.getTitle());
                        if (item.getDescription() != null)
                            Log.d("itemDescription", item.getDescription());
                        if (item.getLink() != null)
                            Log.d("itemLink", item.getLink());
                        if (item.getPubDate() != null)
                            Log.d("itemDate", item.getPubDate());
                        if (item.getThumbnailUrl() != null)
                            Log.d("itemDate", item.getThumbnailUrl());
                    }
                }
            }
        }
    }
    public Document GetData(){
        try{
            url = new URL(address);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            InputStream inputStream = connection.getInputStream();
            DocumentBuilderFactory BuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = BuilderFactory.newDocumentBuilder();
            Document xmlDocument = builder.parse(inputStream);
            return xmlDocument;
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
