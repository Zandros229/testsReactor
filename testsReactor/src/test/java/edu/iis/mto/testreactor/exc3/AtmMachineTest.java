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
        money=Money.builder().withAmount(200).withCurrency(Currency.PL).build();
        card=Card.builder().withCardNumber("12345").withPinNumber(1234).build();
        authenticationToken=AuthenticationToken.builder().withAuthorizationCode(1234).withUserId("12345").build();

        atmMachine = new AtmMachine(cardProviderService, bankService, moneyDepot);

    }

    @Test
    public void ShouldWItchDraw200WhenWithDraw200WithEnoughMoneyOnBankAccountTest(){
        when(cardProviderService.authorize(card)).thenReturn(java.util.Optional.ofNullable(authenticationToken));
        when(bankService.charge(authenticationToken,money)).thenReturn(Boolean.TRUE);
        when(moneyDepot.releaseBanknotes((List<Banknote>)notNull())).thenReturn(Boolean.TRUE);

        List<Banknote> expeted=Banknote.forCurrency(Currency.PL);
        payment=new Payment(expeted.subList(4,5));

        Assertions.assertEquals(payment.getValue(),atmMachine.withdraw(money,card).getValue());
    }
    @Test
    public void ShouldWItchDraw500WhenWithDraw500WithEnoughMoneyOnBankAccountTest(){
        money=Money.builder().withAmount(500).withCurrency(Currency.PL).build();
        when(cardProviderService.authorize(card)).thenReturn(java.util.Optional.ofNullable(authenticationToken));
        when(bankService.charge(authenticationToken,money)).thenReturn(Boolean.TRUE);
        when(moneyDepot.releaseBanknotes((List<Banknote>)notNull())).thenReturn(Boolean.TRUE);

        List<Banknote> expeted=Banknote.forCurrency(Currency.PL);
        payment=new Payment(expeted.subList(5,6));

        Assertions.assertEquals(payment.getValue(),atmMachine.withdraw(money,card).getValue());
    }

    @Test
    public void ShouldWItchDraw200WhenWithDraw200WithCurrencyEUWithEnoughMoneyOnBankAccountTest(){
        money=Money.builder().withAmount(200).withCurrency(Currency.EU).build();
        when(cardProviderService.authorize(card)).thenReturn(java.util.Optional.ofNullable(authenticationToken));
        when(bankService.charge(authenticationToken,money)).thenReturn(Boolean.TRUE);
        when(moneyDepot.releaseBanknotes((List<Banknote>)notNull())).thenReturn(Boolean.TRUE);

        List<Banknote> expeted=Banknote.forCurrency(Currency.EU);
        payment=new Payment(expeted.subList(4,5));

        Assertions.assertEquals(payment.getValue(),atmMachine.withdraw(money,card).getValue());
    }

    @Test
    public void ShouldInvokeMoneyDeptExcetionWhenInvokeWithdrawOnATMMachineWithMoneyDeptProblemsTest(){

        when(cardProviderService.authorize(card)).thenReturn(java.util.Optional.ofNullable(authenticationToken));
        when(bankService.charge(authenticationToken,money)).thenReturn(Boolean.TRUE);

        Assertions.assertThrows(MoneyDepotException.class,()->atmMachine.withdraw(money,card).getValue());
    }

    @Test
    public void ShouldInvokeInsufficientFundsExceptionWhenOnChargeAccDOntHaveEnoghMoney(){
        when(cardProviderService.authorize(card)).thenReturn(java.util.Optional.ofNullable(authenticationToken));

        Assertions.assertThrows(InsufficientFundsException.class,()->atmMachine.withdraw(money,card).getValue());
    }
    @Test
    public void ShouldInvokeCardAutorizationExceptionWhenCardProviderServiceDOntFindTHisCardOrItsBrokenTest(){

        Assertions.assertThrows(CardAuthorizationException.class,()->atmMachine.withdraw(money,card).getValue());
    }

    @Test
    public void ShouldInvokeWrongMoneyAmountExceptionWhenAmountOfMoneyToWithDrawIsLowerThanZeroTest(){
        money=Money.builder().withAmount(-15).withCurrency(Currency.PL).build();

        Assertions.assertThrows(WrongMoneyAmountException.class,()->atmMachine.withdraw(money,card).getValue());
    }

    @Test
    public void ShouldInvokeWrongMoneyAmountExceptionWhenAmountOfMoneyToWithDrawIsThanZeroTest(){
        money=Money.builder().withAmount(0).withCurrency(Currency.PL).build();
        Assertions.assertThrows(WrongMoneyAmountException.class,()->atmMachine.withdraw(money,card).getValue());
    }

    @Test
    public void ShouldInvokeOnceMethodAuthorizeOnCardProviderServiceWhenMakingWithDrawTest(){
        when(cardProviderService.authorize(card)).thenReturn(java.util.Optional.ofNullable(authenticationToken));
        when(bankService.charge(authenticationToken,money)).thenReturn(Boolean.TRUE);
        when(moneyDepot.releaseBanknotes((List<Banknote>)notNull())).thenReturn(Boolean.TRUE);
        atmMachine.withdraw(money,card);


        verify(cardProviderService,times(1)).authorize(card);
    }

    @Test
    public void ShouldInvokeOnceMethodStartTransactionOnBankServiceWhenMakingWithDrawTest(){
        when(cardProviderService.authorize(card)).thenReturn(java.util.Optional.ofNullable(authenticationToken));
        when(bankService.charge(authenticationToken,money)).thenReturn(Boolean.TRUE);
        when(moneyDepot.releaseBanknotes((List<Banknote>)notNull())).thenReturn(Boolean.TRUE);
        atmMachine.withdraw(money,card);


        verify(bankService,times(1)).startTransaction(authenticationToken);
    }

    @Test
    public void ShouldInvokeOnceMethodCommitOnBankServiceWhenMakingWithDrawTest(){
        when(cardProviderService.authorize(card)).thenReturn(java.util.Optional.ofNullable(authenticationToken));
        when(bankService.charge(authenticationToken,money)).thenReturn(Boolean.TRUE);
        when(moneyDepot.releaseBanknotes((List<Banknote>)notNull())).thenReturn(Boolean.TRUE);
        atmMachine.withdraw(money,card);


        verify(bankService,times(1)).commit(authenticationToken);
    }

    @Test
    public void ShouldInvokeOnceMethodChargeOnBankServiceWhenMakingWithDrawTest(){
        when(cardProviderService.authorize(card)).thenReturn(java.util.Optional.ofNullable(authenticationToken));
        when(bankService.charge(authenticationToken,money)).thenReturn(Boolean.TRUE);
        when(moneyDepot.releaseBanknotes((List<Banknote>)notNull())).thenReturn(Boolean.TRUE);
        atmMachine.withdraw(money,card);


        verify(bankService,times(1)).charge(authenticationToken,money);
    }

    @Test
    public void ShouldInvokeOnceMethodReleaseBanknotesOnMoneyDepotWhenMakingWithDrawTest(){
        when(cardProviderService.authorize(card)).thenReturn(java.util.Optional.ofNullable(authenticationToken));
        when(bankService.charge(authenticationToken,money)).thenReturn(Boolean.TRUE);
        when(moneyDepot.releaseBanknotes((List<Banknote>)notNull())).thenReturn(Boolean.TRUE);
        atmMachine.withdraw(money,card);


        verify(moneyDepot,times(1)).releaseBanknotes((List<Banknote>)notNull());
    }

}
