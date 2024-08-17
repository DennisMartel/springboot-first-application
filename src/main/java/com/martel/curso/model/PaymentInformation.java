package com.martel.curso.model;

import jakarta.persistence.*;

class PaymentInformation 
{
    @Column(name = "cardholder_name")
    private String cardHolderName;
    
    @Column(name = "card_number")
    private String cardNumber;
    
    @Column(name = "expiration_date")
    private String expirationDate;
    
    @Column(name = "cvv")
    private String cvv;
}
