package edu.iis.mto.testreactor.exc3;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.internal.matchers.Any;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.*;


@ExtendWith(MockitoExtension.class)
public class AtmMachineTest {

    @Mock
    private CardProviderService cardProviderService;
    @Mock
    private BankService bankService;
    @Mock
    private MoneyDepot moneyDepot;

    private AtmMachine atmMachine;
    private Money money;
    private Card card;
    private AuthenticationToken authenticationToken;
    private Payment payment;

    @BeforeEach
    public void setup() {
        cardProviderService = mock(CardProviderService.class);
        bankService = mock(BankService.class);
        moneyDepot = mock(MoneyDepot.class);


        atmMachine = new AtmMachine(cardProviderService, bankService, moneyDepot);

    }

    @Test
    public void ShouldWItchDraw200WhenWithDraw200WithEnoughMoneyOnBankAccountTest(){
        money=Money.builder().withAmount(200).withCurrency(Currency.PL).build();
        card=Card.builder().withCardNumber("12345").withPinNumber(1234).build();
        authenticationToken=AuthenticationToken.builder().withAuthorizationCode(1234).withUserId("12345").build();
        when(cardProviderService.authorize(card)).thenReturn(java.util.Optional.ofNullable(authenticationToken));
        when(bankService.charge(authenticationToken,money)).thenReturn(Boolean.TRUE);
        when(moneyDepot.releaseBanknotes((List<Banknote>)notNull())).thenReturn(Boolean.TRUE);

        List<Banknote> expeted=Banknote.forCurrency(Currency.PL);
        payment=new Payment(expeted.subList(4,5));

        Assertions.assertEquals(payment.getValue(),atmMachine.withdraw(money,card).getValue());
    }

    @Test
    public void ShouldInvokeMoneyDeptExcetionWhenInvokeWithdrawOnATMMachineWithMoneyDeptProblemsTest(){
        money=Money.builder().withAmount(200).withCurrency(Currency.PL).build();
        card=Card.builder().withCardNumber("12345").withPinNumber(1234).build();
        authenticationToken=AuthenticationToken.builder().withAuthorizationCode(1234).withUserId("12345").build();
        when(cardProviderService.authorize(card)).thenReturn(java.util.Optional.ofNullable(authenticationToken));
        when(bankService.charge(authenticationToken,money)).thenReturn(Boolean.TRUE);

        Assertions.assertThrows(MoneyDepotException.class,()->atmMachine.withdraw(money,card).getValue());
    }

    @Test
    public void ShouldInvokeInsufficientFundsExceptionWhenOnChargeAccDOntHaveEnoghMoney(){
        money=Money.builder().withAmount(200).withCurrency(Currency.PL).build();
        card=Card.builder().withCardNumber("12345").withPinNumber(1234).build();
        authenticationToken=AuthenticationToken.builder().withAuthorizationCode(1234).withUserId("12345").build();
        when(cardProviderService.authorize(card)).thenReturn(java.util.Optional.ofNullable(authenticationToken));

        Assertions.assertThrows(InsufficientFundsException.class,()->atmMachine.withdraw(money,card).getValue());
    }
    @Test
    public void Eror(){
        money=Money.builder().withAmount(200).withCurrency(Currency.PL).build();
        card=Card.builder().withCardNumber("12345").withPinNumber(1234).build();
        authenticationToken=AuthenticationToken.builder().withAuthorizationCode(1234).withUserId("12345").build();
        when(cardProviderService.authorize(card)).thenReturn(java.util.Optional.ofNullable(authenticationToken));

        Assertions.assertThrows(InsufficientFundsException.class,()->atmMachine.withdraw(money,card).getValue());
    }

}
