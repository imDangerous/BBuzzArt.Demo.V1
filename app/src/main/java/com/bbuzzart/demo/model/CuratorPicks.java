package com.bbuzzart.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by imDangerous on 03/02/2017.
 */

@Parcel
@Getter
@Setter
@ToString
public final class CuratorPicks {

    Request request = new Request();
    Response response = new Response();

    @Parcel
    @Getter
    @Setter
    @ToString
    public static class Request {
        // long no;
        // String command;
    }

    @Parcel
    @Getter
    @Setter
    @ToString
    public static class Response {
        @JsonProperty("success")
        boolean success;

        @JsonProperty("message")
        String message;

        @JsonProperty("data")
        List<Datum> data = new ArrayList<>();



        @Parcel
        @Getter
        @Setter
        @ToString
        public static class Datum {

            @JsonProperty("work")
            Work work = new Work();

            String createdDate;
            String croppedImage;
            String feedback;

            @JsonProperty("curator")
            Curator curator = new Curator();


            @Parcel
            @Getter
            @Setter
            @ToString
            public static class Work {

                long id;
                String title;
                String type;
                String abstractText;
                String createdDate;
                int feedbackCount;
                int likeCount;
                boolean bookmarked;
                boolean liked;

                @JsonProperty("createdBy")
                CreatedBy createdBy = new CreatedBy();
                @JsonProperty("attachments")
                List<Attachment> attachments = new ArrayList<>();


                @Parcel
                @Getter
                @Setter
                @ToString
                public static class CreatedBy {
                    long id;
                    String email;
                    String username;
                    String profileImage;
                    String thumbnail;
                    boolean followed;
                }


                @Parcel
                @Getter
                @Setter
                @ToString
                public static class Attachment {

                    int width;
                    int height;
                    String url;

                    Thumbnail thumbnail = new Thumbnail();

                    @Parcel
                    @Getter
                    @Setter
                    @ToString
                    public static class Thumbnail {
                        String small;
                        String medium;
                    }

                }

            }


            @Parcel
            @Getter
            @Setter
            @ToString
            public static class Curator {
                long id;
                String email;
                String username;
                String profileImage;
                String thumbnail;
                boolean followed;
            }

        }

    }

}
