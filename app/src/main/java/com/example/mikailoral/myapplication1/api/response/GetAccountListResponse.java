package com.example.mikailoral.myapplication1.api.response;

import com.example.mikailoral.myapplication1.api.model.AccountDto;

import java.util.List;

/**
 * Created by mikailoral on 9.12.2017.
 */

public class GetAccountListResponse {
    List<AccountDto> accountList;

    public List<AccountDto> getAccountList() {
        return accountList;
    }

    public void setAccountList(List<AccountDto> accountList) {
        this.accountList = accountList;
    }
}
