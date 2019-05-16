package edu.iis.mto.testreactor.exc3;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.mock;


@ExtendWith(MockitoExtension.class)
    public class AtmMachineTest {

        @Mock
        private CardProviderService cardProviderService;
        @Mock
        private BankService bankService;
        @Mock
        private MoneyDepot moneyDepot;

        private AtmMachine atmMachine;

        @BeforeEach
        public void setup(){
            cardProviderService=mock(CardProviderService.class);
            bankService=mock(BankService.class);
            moneyDepot=mock(MoneyDepot.class);
        }



}
