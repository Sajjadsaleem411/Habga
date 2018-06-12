package com.karigar.hubgaclient.Model;

import java.util.List;

/**
 * Created by Administrator on 5/6/2018.
 */

public class TicketModel {
    int ticketID;
    String type;
    String title;
    public int[] data;
    int BookingID;
    int catID;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getTicketID() {
        return ticketID;
    }

    public void setTicketID(int ticketID) {
        this.ticketID = ticketID;
    }
    public int getBookingID() {
        return BookingID;
    }

    public void setBookingID(int bookingID) {
        this.BookingID = bookingID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getCatID() {
        return catID;
    }

    public void setCatID(int catID) {
        this.catID = catID;
    }

}
