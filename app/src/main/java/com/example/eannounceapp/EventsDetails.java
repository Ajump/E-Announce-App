package com.example.eannounceapp;

import java.io.Serializable;
import java.util.List;

public class EventsDetails implements Serializable {
    private String eventID;
    private String keywordSoundex;
    private String userId;
    private String eventName;
    private String eventDateStart;
    private String eventDateEnd;
    private String eventTimeStart;
    private String eventTimeEnd;
    private String eventCategory;
    private List<String> eventLocation;
    private String eventOrganizer;
    private String eventSpeaker;
    private String eventDescription;
    private String eventPoster;
    private Integer eventInterested;
    private List<String> contactPerson1;
    private List<String> contactPerson2;
    private Boolean foodAvailability;
    private Boolean couponAvailability;
    private String eventStatus;


    public EventsDetails() {
    }

    public EventsDetails(String eventID, String keywordSoundex, String userId, String eventName,
                         String eventDateStart, String eventDateEnd, String eventTimeStart,
                         String eventTimeEnd, String eventCategory, List<String> eventLocation,
                         String eventOrganizer, String eventSpeaker, String eventDescription,
                         String eventPoster, Integer eventInterested, List<String> contactPerson1,
                         List<String> contactPerson2, Boolean foodAvailability, Boolean couponAvailability,
                         String eventStatus) {

        this.eventID = eventID;
        this.keywordSoundex = keywordSoundex;
        this.userId = userId;
        this.eventName = eventName;
        this.eventDateStart = eventDateStart;
        this.eventDateEnd = eventDateEnd;
        this.eventTimeStart = eventTimeStart;
        this.eventTimeEnd = eventTimeEnd;
        this.eventCategory = eventCategory;
        this.eventLocation = eventLocation;
        this.eventOrganizer = eventOrganizer;
        this.eventSpeaker = eventSpeaker;
        this.eventDescription = eventDescription;
        this.eventPoster = eventPoster;
        this.eventInterested = eventInterested;
        this.contactPerson1 = contactPerson1;
        this.contactPerson2 = contactPerson2;
        this.foodAvailability = foodAvailability;
        this.couponAvailability = couponAvailability;
        this.eventStatus = eventStatus;
    }

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public String getKeywordSoundex() {
        return keywordSoundex;
    }

    public String getUserId() {
        return userId;
    }

    public String getEventName() {
        return eventName;
    }

    public String getEventDateStart() {
        return eventDateStart;
    }

    public String getEventDateEnd() {
        return eventDateEnd;
    }

    public String getEventTimeStart() {
        return eventTimeStart;
    }

    public String getEventTimeEnd() {
        return eventTimeEnd;
    }

    public String getEventCategory() {
        return eventCategory;
    }

    public List<String> getEventLocation() {
        return eventLocation;
    }

    public String getEventOrganizer() {
        return eventOrganizer;
    }

    public String getEventSpeaker() {
        return eventSpeaker;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public String getEventPoster() {
        return eventPoster;
    }

    public Integer getEventInterested() {
        return eventInterested;
    }

    public List<String> getContactPerson1() {
        return contactPerson1;
    }

    public List<String> getContactPerson2() {
        return contactPerson2;
    }

    public Boolean getFoodAvailability() {
        return foodAvailability;
    }

    public Boolean getCouponAvailability() {
        return couponAvailability;
    }

    public String getEventStatus() {
        return eventStatus;
    }

    @Override
    public String toString() {
        return "EventsDetails{" +
                "keywordSoundex='" + keywordSoundex + '\'' +
                ", userId='" + userId + '\'' +
                ", eventName='" + eventName + '\'' +
                ", eventDateStart='" + eventDateStart + '\'' +
                ", eventDateEnd='" + eventDateEnd + '\'' +
                ", eventTimeStart='" + eventTimeStart + '\'' +
                ", eventTimeEnd='" + eventTimeEnd + '\'' +
                ", eventCategory='" + eventCategory + '\'' +
                ", eventLocation='" + eventLocation + '\'' +
                ", eventOrganizer='" + eventOrganizer + '\'' +
                ", eventSpeaker='" + eventSpeaker + '\'' +
                ", eventDescription='" + eventDescription + '\'' +
                ", eventPoster='" + eventPoster + '\'' +
                ", eventInterested=" + eventInterested +
                ", contactPerson1=" + contactPerson1 +
                ", contactPerson2=" + contactPerson2 +
                ", foodAvailability=" + foodAvailability +
                ", couponAvailability=" + couponAvailability +
                ", eventStatus='" + eventStatus + '\'' +
                '}';
    }
}
