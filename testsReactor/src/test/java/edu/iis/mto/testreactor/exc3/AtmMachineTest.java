package edu.iis.mto.testreactor.exc3;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
public class AtmMachineTest {

    @Mock
    private CardProviderService cardProviderService;
    @Mock
    private BankService bankService;
    @Mock
    private MoneyDepot moneyDepot;

    private AtmMachine atmMachine;




}
