package model;

import java.time.LocalTime;

public class OpeningHours {
        LocalTime opening;
        LocalTime closing;
        boolean open24=false;
        boolean closeDay=false;
        public OpeningHours(String opening, String closing){

            if (opening=="24")
                open24=true;
            else if (opening!=null){
                this.opening = LocalTime.parse(opening);
                this.closing = LocalTime.parse(closing);
            }
            else
                closeDay=true;
        }
    }
