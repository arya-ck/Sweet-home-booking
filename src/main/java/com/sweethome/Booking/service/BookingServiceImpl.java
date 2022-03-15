package com.sweethome.Booking.service;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;
import com.sweethome.Booking.dto.BookingInfoDto;
import com.sweethome.Booking.dto.BookingPaymentInfoDTO;
import com.sweethome.Booking.dto.InvalidPaymentMessageDTO;
import com.sweethome.Booking.entities.BookingInfoEntity;
import com.sweethome.Booking.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.Period;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BookingServiceImpl implements BookingService{

    @Autowired
    BookingRepository bookingRepository;

    @Autowired
    EurekaClient eurekaClient;

    public static ArrayList<String> getRandomNumbers(int count){
        Random rand = new Random();
        int upperBound = 100;
        ArrayList<String>numberList = new ArrayList<String>();

        for (int i=0; i<count; i++){
            numberList.add(String.valueOf(rand.nextInt(upperBound)));
        }

        return numberList;
    }

    public int calculateRoomPrice(int numOfRooms, int numOfDays){
        return 1000 * numOfRooms * numOfDays;
    }

    public BookingInfoEntity bookHotel(BookingInfoEntity bookingInfo){
        BookingInfoEntity savedBooking = null;

        // calculate total price
        int days = Period.between(bookingInfo.getToDate(), bookingInfo.getFromDate()).getDays();
        bookingInfo.setRoomPrice(this.calculateRoomPrice(bookingInfo.getNumOfRooms(), days));

        // generate random room numbers
        List<String> rooms = this.getRandomNumbers(bookingInfo.getNumOfRooms());
        String roomList = rooms.stream().collect(Collectors.joining(","));
        bookingInfo.setRoomNumbers(roomList);

        // set booking time
        bookingInfo.setBookedOn(LocalDateTime.now());

        // save booking
        savedBooking = this.bookingRepository.save(bookingInfo);

        return savedBooking;
    }

    public BookingInfoEntity doPayment(int bookingId, BookingPaymentInfoDTO paymentInfoDTO){

        // get details from database
        BookingInfoEntity bookingInfoEntity = this.bookingRepository.findById(bookingId).get();

        // get api gateway details
        Application apiGateway = eurekaClient.getApplication("API-GATEWAY");
        InstanceInfo instanceInfo = apiGateway.getInstances().get(0);
        String hostname = instanceInfo.getHostName();
        int port = instanceInfo.getPort();
        String url = "http://"+hostname+":"+port+"/payment/transaction";

        // make payment
        RestTemplate payementRequestTemplate = new RestTemplate();
        Map<String, String> requestParams = new HashMap<>();
        requestParams.put("bookingId", String.valueOf(bookingInfoEntity.getBookingId()));
        requestParams.put("paymentMode", paymentInfoDTO.getPaymentMode());
        requestParams.put("upiId", paymentInfoDTO.getUpiId());
        requestParams.put("cardNumber", paymentInfoDTO.getCardNumber());

        ResponseEntity<Integer> transactionResponse = payementRequestTemplate.postForEntity(url, requestParams, Integer.class);
        int transactionId = transactionResponse.getBody();
        String message = "Booking confirmed for user with aadhaar number: "
                + bookingInfoEntity.getAadharNumber()
                +    "    |    "
                + "Here are the booking details:    " + bookingInfoEntity.toString();
        System.out.println(message);

        // save transaction id
        bookingInfoEntity.setTransactionId(transactionId);
        BookingInfoEntity savedBooking = bookingRepository.save(bookingInfoEntity);

        return savedBooking;
    }

    public boolean checkBookingValidity(int bookingId){

        // get details from database
        Optional<BookingInfoEntity> bookingInfoEntity = this.bookingRepository.findById(bookingId);
        if(bookingInfoEntity.isPresent()){
            return true;
        }
        return false;
    }
}
