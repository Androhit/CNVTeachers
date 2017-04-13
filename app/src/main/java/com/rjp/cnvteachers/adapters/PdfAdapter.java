package com.rjp.cnvteachers.adapters;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;

import com.itextpdf.text.Anchor;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Section;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.rjp.cnvteachers.beans.AchievementsBean;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.github.mikephil.charting.charts.Chart.LOG_TAG;

/**
 * Created by Shraddha on 4/13/2017.
 */

public class PdfAdapter extends ArrayAdapter<AchievementsBean> {
    public ArrayList<AchievementsBean> taskList;
    public ArrayList<AchievementsBean> arraylist;
    public Context mContext;
    LayoutInflater flater;
    public ArrayList<AchievementsBean> arr;

    public PdfAdapter(Activity context, int resouceId, int textviewId, ArrayList<AchievementsBean> list) {

        super(context, resouceId, textviewId, list);
        flater = context.getLayoutInflater();
        this.taskList = arr;
        this.arraylist = new ArrayList<AchievementsBean>();
        this.arraylist.addAll(arr);
        this.mContext = context;
    }

    public void create_pdf(ArrayList<AchievementsBean> arr) throws FileNotFoundException, DocumentException {
        File pdfFolder = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS), "pdfdemo");
        if (!pdfFolder.exists()) {
            pdfFolder.mkdir();
            Log.i(LOG_TAG, "Pdf Directory created"+pdfFolder);
        }

        //Create time stamp
        Date date = new Date() ;
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(date);

        File myFile = new File(pdfFolder + timeStamp + ".pdf");
        Log.i(LOG_TAG, "Pdf File created"+myFile);

        OutputStream output = new FileOutputStream(myFile);
        Document document = new Document();
        PdfWriter.getInstance(document, output);
        Log.i(LOG_TAG, "Pdf File opening"+myFile);
        document.open();
        int n;
        for(n=0; n<arr.size(); n++)
        {
            String v= String.valueOf(arr.get(n));
            document.add(new Paragraph(v));
            Log.i(LOG_TAG, "Pdf data added for n "+document);

        }
        Log.i(LOG_TAG, "Pdf data added"+document);
     //   addContent(document);
        document.close();
    }

    private void addContent(Document document) throws DocumentException {
        Anchor anchor = new Anchor("First Chapter");
        anchor.setName("PDF");

        // Second parameter is the number of the chapter
        Chapter catPart = new Chapter(new Paragraph(anchor), 1);

        Paragraph subPara = new Paragraph("Espalier");
        Section subCatPart = catPart.addSection(subPara);
        subCatPart.add(new Paragraph("Espalier"));
        createTable(subCatPart);
        document.add(catPart);
    }

    private void createTable(Section subCatPart) throws BadElementException {
        PdfPTable table = new PdfPTable(4);

        PdfPCell c1 = new PdfPCell(new Phrase("Achievment"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Event Name"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Venue"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Organized By"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        table.setHeaderRows(1);
        for (AchievementsBean wp : arr) {

            table.addCell(new Paragraph(wp.getAchivement()));
            table.addCell(new Paragraph(wp.getEvent_name()));
            table.addCell(new Paragraph(wp.getVenu()));
            table.addCell(new Paragraph(wp.getOrganized_by()));
            subCatPart.add(table);
        }
    }
}
