package vn.ute.smartphoneshop.service;

import vn.ute.smartphoneshop.entity.VoucherEntity;

import java.util.List;

public interface IVoucherService {
    VoucherEntity findVoucherByCode(String code);
    boolean isVoucherValid(VoucherEntity voucher);
    List<String> getAllVoucherNames();
    List<Float>getAllVoucherValues();
}
