//package com.example.demo.dto;
//
//import lombok.Getter;
//import lombok.Setter;
//
//@Getter
//@Setter
//public class BookingRequestDto {
//    private Long movieId;
//    private int ticketCount;
//    private String seatInfo;
//}

// src/main/java/com/example/demo/dto/BookingRequestDto.java
package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BookingRequestDto {
    private Long screeningId;
    private List<String> seats;
}
