package vn.ute.smartphoneshop.service;

import vn.ute.smartphoneshop.entity.PaymentEntity;

public interface IPaymentService {
    PaymentEntity findPaymentMethod(String PaymentName);
}
