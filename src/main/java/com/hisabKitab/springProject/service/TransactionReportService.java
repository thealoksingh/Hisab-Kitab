package com.hisabKitab.springProject.service;

import com.hisabKitab.springProject.entity.Transaction;
import com.hisabKitab.springProject.entity.UserEntity;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import java.awt.Color; // Import java.awt.Color
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class TransactionReportService {

    public byte[] generateFriendTransactionReport(List<Transaction> transactions, UserEntity friend, String fromDate, String toDate, double openingBalance, double totalDebit, double totalCredit) throws IOException {
        try {
            Document document = new Document(PageSize.A4);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfWriter writer = PdfWriter.getInstance(document, baos);
            document.open();

            Font font = FontFactory.getFont(FontFactory.HELVETICA, 10);
            Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10);
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20);
            
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy");
         // Parse the input string to a Date object
//            Date date = outputFormat.format(inputFormat.parse(fromDate));
//
//            // Format the Date object to the desired format
//            String formattedDate = outputFormat.format(date);

            // Header
            Paragraph header = new Paragraph(friend.getFullName() + "'s Transaction Statement", headerFont); // Dynamic friend name
            header.setAlignment(Element.ALIGN_CENTER);
            document.add(header);

//            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); // Define the date pattern
////            Date date = formatter.parse(dateString);
//            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
            String dateRange = "(" + outputFormat.format(inputFormat.parse(fromDate)) +" to " +  outputFormat.format(inputFormat.parse(toDate)) + ")"; // Current date as range (you can customize)
            Paragraph dateRangeParagraph = new Paragraph(dateRange, font);
            dateRangeParagraph.setAlignment(Element.ALIGN_CENTER);
            document.add(dateRangeParagraph);

            document.add(Chunk.NEWLINE);

            // Summary Table
            
            
            // Parse the input date string to a Date object
            Date date = inputFormat.parse(fromDate);

            // Use Calendar to subtract one day
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DAY_OF_MONTH, -1);

            // Get the day before as a Date object
            Date dayBeforeDate = calendar.getTime();

            // Format the day-before date back to string
            String formattedDayBeforeDate = outputFormat.format(dayBeforeDate);

            // Print the result
            System.out.println("Day Before Date: " + formattedDayBeforeDate);

            PdfPTable summaryTable = new PdfPTable(4);
            summaryTable.setWidthPercentage(100);
            addTableCell(summaryTable, "Opening Balance\n" + String.format("%.2f", openingBalance) +"\n(On "+formattedDayBeforeDate+")", boldFont, Color.LIGHT_GRAY); // Format to 2 decimal places
            addTableCell(summaryTable, "Total Debit (-)\n" + String.format("%.2f", totalDebit), boldFont, Color.LIGHT_GRAY);
            addTableCell(summaryTable, "Total Credit (+)\n" + String.format("%.2f", totalCredit), boldFont, Color.LIGHT_GRAY);
            double netBalance = totalCredit - totalDebit;
            addTableCell(summaryTable, "Net Balance\n" + String.format("%.2f", netBalance), boldFont, Color.LIGHT_GRAY);
            document.add(summaryTable);
            document.add(Chunk.NEWLINE);

            // Entries Table
            PdfPTable entriesTable = new PdfPTable(4);
            entriesTable.setWidthPercentage(100);

            addTableCell(entriesTable, "Date", boldFont, Color.LIGHT_GRAY);
            addTableCell(entriesTable, "Details", boldFont, Color.LIGHT_GRAY);
            addTableCell(entriesTable, "Debit(-)", boldFont, Color.LIGHT_GRAY);
            addTableCell(entriesTable, "Credit(+)", boldFont, Color.LIGHT_GRAY);

            // Table Rows (using the provided transaction list)
            for (Transaction transaction : transactions) {
            	double debitAmount = 0.0d;
            	double creditAmount = 0.0d;
            	if(transaction.getFromUserId() == friend.getUserId()) {
            		creditAmount = transaction.getAmount();
            	} else{
            		debitAmount = transaction.getAmount();
            	}
//                SimpleDateFormat transactionDateFormat = new SimpleDateFormat("dd MMM yyyy");
//                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
//                SimpleDateFormat outputFormat = new SimpleDateFormat("dd MM yyyy");
                // Parse the input string to a Date object
                 date = inputFormat.parse(fromDate);

                // Format the Date object to the desired format
                String formattedDate = outputFormat.format(date);
//                String transactionDate = transactionDateFormat.format(transaction.getTransDate());
                String transactionDate = transaction.getTransDate().toString();
                addTableRow(entriesTable, formattedDate, transaction.getDescription(), String.format("%.2f", debitAmount), String.format("%.2f", creditAmount), font);
            }

            document.add(entriesTable);

            document.add(Chunk.NEWLINE);

            // Grand Total
			/*
			 * PdfPTable grandTotalTable = new PdfPTable(2);
			 * grandTotalTable.setWidthPercentage(100); addTableCell(grandTotalTable,
			 * "Grand Total", boldFont, Color.LIGHT_GRAY); addTableCell(grandTotalTable,
			 * String.format("%.2f", totalDebit)+
			 * "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"
			 * +String.format("%.2f", totalCredit), boldFont, Color.LIGHT_GRAY);
			 * document.add(grandTotalTable);
			 */
            
         // Grand Total
            PdfPTable grandTotalTable = new PdfPTable(3); // Use 3 columns
            grandTotalTable.setWidthPercentage(100);
            grandTotalTable.setWidths(new float[]{2, 1, 1}); // Set relative column widths

            // Add a "Grand Total" label spanning 2 cells
            PdfPCell labelCell = new PdfPCell(new Phrase("Grand Total", boldFont));
//            labelCell.setColspan(2); // Span the first two columns
            labelCell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
            labelCell.setBackgroundColor(Color.LIGHT_GRAY);
            grandTotalTable.addCell(labelCell);

            // Add the total debit cell
            PdfPCell debitCell = new PdfPCell(new Phrase(String.format("%.2f", totalDebit), boldFont));
            debitCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            debitCell.setBackgroundColor(Color.RED);
            grandTotalTable.addCell(debitCell);

            // Add the total credit cell
            PdfPCell creditCell = new PdfPCell(new Phrase(String.format("%.2f", totalCredit), boldFont));
            creditCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            creditCell.setBackgroundColor(Color.GREEN);
            grandTotalTable.addCell(creditCell);

            // Add the table to the document
            document.add(grandTotalTable);



            document.close();
            writer.close();
            return baos.toByteArray();

        } catch (DocumentException | ParseException   e) {
            e.printStackTrace(); // Handle the exception appropriately in your application
            return null; // Or throw a custom exception
        }
    }

    private void addTableCell(PdfPTable table, String text, Font font, Color bgColor) { // Use java.awt.Color
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBackgroundColor(bgColor);
        cell.setPadding(5);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
    }

    private void addTableRow(PdfPTable table, String date, String details, String debit, String credit, Font font) {
        addTableCell(table, date, font, Color.WHITE); // Use Color.WHITE
        addTableCell(table, details, font, Color.WHITE);
        addTableCell(table, debit, font, Color.RED);
        addTableCell(table, credit, font, Color.GREEN);
    }
}
