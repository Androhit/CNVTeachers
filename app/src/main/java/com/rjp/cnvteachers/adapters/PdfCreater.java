package com.rjp.cnvteachers.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.itextpdf.text.Chapter;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.rjp.cnvteachers.beans.AchievementsBean;
import com.rjp.cnvteachers.beans.AttendanceBean;
import com.rjp.cnvteachers.beans.ExamDetailsBeans;
import com.rjp.cnvteachers.beans.ExamResultsBean;
import com.rjp.cnvteachers.beans.HandsOnScienceBeans;

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

public class PdfCreater {
    public Context mContext;
    File myFile;
    AchievementsBean obj;
    Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18,Font.BOLD);
    Font tabFont = new Font(Font.FontFamily.TIMES_ROMAN, 16,Font.BOLD);

    public PdfCreater(Context context) // for Achievements
    {
        this.mContext = context;
    }

     public void create_pdf_achievement(ArrayList<AchievementsBean> arr) throws FileNotFoundException, DocumentException {
        File pdfFolder = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS), "pdfdemo");
        if (!pdfFolder.exists()) {
            pdfFolder.mkdir();
            Log.e(LOG_TAG, "Pdf Directory created"+pdfFolder);
        }
        //Create time stamp
        Date date = new Date() ;
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(date);

        myFile = new File(pdfFolder + timeStamp + ".pdf");
        Log.e(LOG_TAG, "Pdf File created"+myFile);

        OutputStream output = new FileOutputStream(myFile);
        Document document = new Document();
        PdfWriter.getInstance(document, output);
        Log.e(LOG_TAG, "Pdf File opening"+myFile);
        document.open();

         Paragraph p=new Paragraph("Achievement Report",catFont);
         p.setAlignment(Element.ALIGN_CENTER);

         Paragraph p1=new Paragraph("  ");
         p1.setAlignment(Element.ALIGN_CENTER);

         Chapter catPart = new Chapter(1);

         catPart.add(p);
         catPart.add(p1);
       // createTable(subCatPart,arr);
        PdfPTable table = new PdfPTable(4);

        PdfPCell c1 = new PdfPCell(new Phrase("Achievment",tabFont));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Event Name",tabFont));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Venue",tabFont));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Organized By",tabFont));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        table.setHeaderRows(1);

        for (AchievementsBean wp : arr) {

            table.addCell(new Paragraph(wp.getAchivement()));
            table.addCell(new Paragraph(wp.getEvent_name()));
            table.addCell(new Paragraph(wp.getVenu()));
            table.addCell(new Paragraph(wp.getOrganized_by()));
        }
        catPart.add(table);

        document.add(catPart);

        Log.e(LOG_TAG, "Pdf data added"+document);
        //addContent(document);

        document.close();
        promptForNextAction();
    }

    public void create_pdf_attendance(ArrayList<AttendanceBean> arr) throws FileNotFoundException, DocumentException {
        File pdfFolder = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS), "pdfdemo");
        if (!pdfFolder.exists()) {
            pdfFolder.mkdir();
            Log.e(LOG_TAG, "Pdf Directory created"+pdfFolder);
        }

        //Create time stamp
        Date date = new Date() ;
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(date);

        myFile = new File(pdfFolder + timeStamp + ".pdf");
        Log.e(LOG_TAG, "Pdf File created"+myFile);

        OutputStream output = new FileOutputStream(myFile);
        Document document = new Document();
        PdfWriter.getInstance(document, output);
        Log.e(LOG_TAG, "Pdf File opening"+myFile);
        document.open();
        Paragraph p=new Paragraph("Attendance Report",catFont);
        p.setAlignment(Element.ALIGN_CENTER);

        Paragraph p1=new Paragraph("  ");
        p1.setAlignment(Element.ALIGN_CENTER);

        Chapter catPart = new Chapter(1);

        catPart.add(p);
        catPart.add(p1);

        // createTable(subCatPart,arr);
        PdfPTable table = new PdfPTable(5);

        PdfPCell c1 = new PdfPCell(new Phrase("Student Name",tabFont));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Working Days",tabFont));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Present Days",tabFont));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Absent days",tabFont));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Percentage(%)",tabFont));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        table.setHeaderRows(1);

        for (AttendanceBean wp : arr) {

            table.addCell(new Paragraph(wp.getName()));
            table.addCell(new Paragraph(wp.getWorking_days()));
            table.addCell(new Paragraph(wp.getPercent()));
            table.addCell(new Paragraph(wp.getAbsent_days()));
            table.addCell(new Paragraph(wp.getPercent()));
        }
        catPart.add(table);

        document.add(catPart);

        Log.e(LOG_TAG, "Pdf data added"+document);
        //addContent(document);

        document.close();
        promptForNextAction();

    }

    public void create_pdf_handson(ArrayList<HandsOnScienceBeans> arr) throws FileNotFoundException, DocumentException {
        File pdfFolder = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS), "pdfdemo");
        if (!pdfFolder.exists()) {
            pdfFolder.mkdir();
            Log.e(LOG_TAG, "Pdf Directory created"+pdfFolder);
        }

        //Create time stamp
        Date date = new Date() ;
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(date);

        myFile = new File(pdfFolder + timeStamp + ".pdf");
        Log.e(LOG_TAG, "Pdf File created"+myFile);

        OutputStream output = new FileOutputStream(myFile);
        Document document = new Document();
        PdfWriter.getInstance(document, output);
        Log.e(LOG_TAG, "Pdf File opening"+myFile);
        document.open();
        Paragraph p=new Paragraph("Hands On Science Report",catFont);
        p.setAlignment(Element.ALIGN_CENTER);

        Paragraph p1=new Paragraph("  ");
        p1.setAlignment(Element.ALIGN_CENTER);

        Chapter catPart = new Chapter(1);

        catPart.add(p);
        catPart.add(p1);


        // createTable(subCatPart,arr);
        PdfPTable table = new PdfPTable(5);

        PdfPCell c1 = new PdfPCell(new Phrase("From Date",tabFont));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("To Date",tabFont));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Project Title",tabFont));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Project Aim",tabFont));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Project Description",tabFont));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        table.setHeaderRows(1);

        for (HandsOnScienceBeans wp : arr) {

            table.addCell(new Paragraph(wp.getFrom_date()));
            table.addCell(new Paragraph(wp.getTo_date()));
            table.addCell(new Paragraph(wp.getProj_title()));
            table.addCell(new Paragraph(wp.getProj_aim()));
            table.addCell(new Paragraph(wp.getProj_disc()));
        }
        catPart.add(table);

        document.add(catPart);

        Log.e(LOG_TAG, "Pdf data added"+document);
        //addContent(document);

        document.close();
        promptForNextAction();

    }
    public void create_pdf_examtimetable(ArrayList<ExamDetailsBeans> arr) throws FileNotFoundException, DocumentException {
        File pdfFolder = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS), "pdfdemo");
        if (!pdfFolder.exists()) {
            pdfFolder.mkdir();
            Log.e(LOG_TAG, "Pdf Directory created"+pdfFolder);
        }

        //Create time stamp
        Date date = new Date() ;
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(date);

        myFile = new File(pdfFolder + timeStamp + ".pdf");
        Log.e(LOG_TAG, "Pdf File created"+myFile);

        OutputStream output = new FileOutputStream(myFile);
        Document document = new Document();
        PdfWriter.getInstance(document, output);
        Log.e(LOG_TAG, "Pdf File opening"+myFile);
        document.open();
        //Anchor anchor = new Anchor("Exam Time table",catFont);
      //  anchor.setName("PDF");

        Paragraph p=new Paragraph("Exam Time table",catFont);
        p.setAlignment(Element.ALIGN_CENTER);

        Paragraph p1=new Paragraph("  ");
        p1.setAlignment(Element.ALIGN_CENTER);

        Chapter catPart = new Chapter(1);

        catPart.add(p);
        catPart.add(p1);

        // createTable(subCatPart,arr);
        PdfPTable table = new PdfPTable(4);

        PdfPCell c1 = new PdfPCell(new Phrase("Subject",tabFont));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Date",tabFont));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("From Time",tabFont));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("To Time",tabFont));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        table.setHeaderRows(1);

        for (ExamDetailsBeans wp : arr) {

            table.addCell(new Paragraph(wp.getSub_name()));
            table.addCell(new Paragraph(wp.getExam_date()));
            table.addCell(new Paragraph(wp.getTime_from()));
            table.addCell(new Paragraph(wp.getTime_to()));
        }
        catPart.add(table);

        document.add(catPart);

        Log.e(LOG_TAG, "Pdf data added"+document);
        //addContent(document);

        document.close();
        promptForNextAction();

    }
    public void create_pdf_performance(ArrayList<ExamResultsBean> arr) throws FileNotFoundException, DocumentException {
        File pdfFolder = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS), "pdfdemo");
        if (!pdfFolder.exists()) {
            pdfFolder.mkdir();
            Log.e(LOG_TAG, "Pdf Directory created"+pdfFolder);
        }

        //Create time stamp
        Date date = new Date() ;
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(date);

        myFile = new File(pdfFolder + timeStamp + ".pdf");
        Log.e(LOG_TAG, "Pdf File created"+myFile);

        OutputStream output = new FileOutputStream(myFile);
        Document document = new Document();
        PdfWriter.getInstance(document, output);
        Log.e(LOG_TAG, "Pdf File opening"+myFile);
        document.open();
        Paragraph p=new Paragraph("Performance Report",catFont);
        p.setAlignment(Element.ALIGN_CENTER);

        Paragraph p1=new Paragraph("  ");
        p1.setAlignment(Element.ALIGN_CENTER);

        Chapter catPart = new Chapter(1);

        catPart.add(p);
        catPart.add(p1);

        // createTable(subCatPart,arr);
        PdfPTable table = new PdfPTable(5);

        PdfPCell c1 = new PdfPCell(new Phrase("Subject",tabFont));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Marks",tabFont));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Max marks",tabFont));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Percentage(%)",tabFont));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Grade",tabFont));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        table.setHeaderRows(1);
        String obtained_marks="",total="",per="",grade="";
        for (ExamResultsBean wp : arr) {

            table.addCell(new Paragraph(wp.getSub_name()));
            table.addCell(new Paragraph(wp.getMarks()));
            table.addCell(new Paragraph(wp.getMax_marks()));
            table.addCell(new Paragraph(wp.getPercentage()));
            table.addCell(new Paragraph(wp.getGrade()));

            obtained_marks=wp.getObtained_marks();
            total=wp.getTotal_marks();
            per=wp.getTotal_percentage();
            grade=wp.getTotal_grade();
        }

        table.addCell(new Paragraph("Total",tabFont));
        table.addCell(new Paragraph(obtained_marks,tabFont));
        table.addCell(new Paragraph(total,tabFont));
        table.addCell(new Paragraph(per,tabFont));
        table.addCell(new Paragraph(grade,tabFont));
        catPart.add(table);
        document.add(catPart);

        Log.e(LOG_TAG, "Pdf data added"+document);
        //addContent(document);

        document.close();
        promptForNextAction();

    }



    public void promptForNextAction()

    {
        final String[] options = { "Preview","Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Pdf Saved, What Next?");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (options[which].equals("Preview")){
                    viewPdf();
                }else if (options[which].equals("Cancel")){
                    dialog.dismiss();
                }
            }
        });

        builder.show();

    }

    public void viewPdf(){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(myFile), "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        mContext.startActivity(intent);
    }

}
