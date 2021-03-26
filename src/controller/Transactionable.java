package controller;

import model.Address;

public interface Transactionable {
    public boolean action(Address address);
}