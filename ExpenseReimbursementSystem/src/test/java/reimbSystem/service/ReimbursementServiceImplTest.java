package reimbSystem.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import reimbsystem.dao.ReimbursementDao;
import reimbsystem.model.Reimbursement;
import reimbsystem.model.Reimbursement.ReimbursementStatus;
import reimbsystem.model.User;
import reimbsystem.service.ReimbursementServiceImpl;

public class ReimbursementServiceImplTest {

	ReimbursementDao reimbDaoMock;
	ReimbursementServiceImpl reimbService;
	User testUser;
	
	@BeforeEach
	void setUp() throws Exception {

		reimbDaoMock = Mockito.mock(ReimbursementDao.class);
		
		reimbService = new ReimbursementServiceImpl();
		reimbService.setReimbDao(reimbDaoMock);
		
		testUser = new User();
	}

	@Test
	void testInsertReimbursement() {
		Mockito.when(reimbDaoMock.insertReimbursement(any(Reimbursement.class))).thenReturn(true);
		
		// Vanilla test
		boolean added = reimbService.insertReimbursement(1, "test", "LODGING", testUser);
		assertEquals(true, added, "Failed vanilla test");
		
		// Negative amount
		added = reimbService.insertReimbursement(-1, "test", "LODGING", testUser);
		assertEquals(false, added, "Failed negative amount test");

		// Zero amount
		added = reimbService.insertReimbursement(0, "test", "LODGING", testUser);
		assertEquals(false, added, "Failed zero amount test");
		
		// Null author
		added = reimbService.insertReimbursement(1, "test", "LODGING", null);
		assertEquals(false, added, "Failed null author test");
		
		// Invalid reimbursement type (should still return true)
		added = reimbService.insertReimbursement(1, "test", "TESTING_GARBAGE", testUser);
		assertEquals(true, added, "Failed invalid reimbursement test");
		
	}
	
	@Test
	void testselectReimbursementsByEmployee() {
		List<Reimbursement> successList = new ArrayList<Reimbursement>();
		Mockito.when(reimbDaoMock.selectReimbursementsByEmployee(1)).thenReturn(successList);
		
		// For testing purposes invalid values will also return the successList
		Mockito.when(reimbDaoMock.selectReimbursementsByEmployee(-1)).thenReturn(successList);
		Mockito.when(reimbDaoMock.selectReimbursementsByEmployee(0)).thenReturn(successList);
		
		// Vanilla test
		List<Reimbursement> reimbList = reimbService.selectReimbursementsByEmployee(1);
		assertEquals(successList, reimbList, "Failed vanilla test");
		
		// Negative id test
		reimbList = reimbService.selectReimbursementsByEmployee(-1);
		assertEquals(null, reimbList, "Failed negative id test");
		
		// Zero id test
		reimbList = reimbService.selectReimbursementsByEmployee(0);
		assertEquals(null, reimbList, "Failed zero id test");
	}
	
	@Test
	void testSelectReimbursementByID() {
		Reimbursement successReimb = new Reimbursement();
		Mockito.when(reimbDaoMock.selectReimbursementByID(1)).thenReturn(successReimb);
		
		// For testing purposes invalid values will also return the successReimb
		Mockito.when(reimbDaoMock.selectReimbursementByID(-1)).thenReturn(successReimb);
		Mockito.when(reimbDaoMock.selectReimbursementByID(0)).thenReturn(successReimb);
		
		// Vanilla test
		Reimbursement testReimb = reimbService.selectReimbursementByID(1);
		assertEquals(successReimb, testReimb, "Failed vanilla test");
		
		// Negative id test
		testReimb = reimbService.selectReimbursementByID(-1);
		assertEquals(null, testReimb, "Failed negative id test");
		
		// Zero id test
		testReimb = reimbService.selectReimbursementByID(0);
		assertEquals(null, testReimb, "Failed zero id test");
	}
	
	@Test
	void testUpdateReimbursement() {
		Mockito.when(reimbDaoMock.updateReimbursement(eq(1), eq(ReimbursementStatus.APPROVED), any(Timestamp.class), eq(testUser))).thenReturn(true);
		
		// For testing purposes invalid values will also return true
		Mockito.when(reimbDaoMock.updateReimbursement(eq(-1), eq(ReimbursementStatus.APPROVED), any(Timestamp.class), eq(testUser))).thenReturn(true);
		Mockito.when(reimbDaoMock.updateReimbursement(eq(0), eq(ReimbursementStatus.APPROVED), any(Timestamp.class), eq(testUser))).thenReturn(true);
		Mockito.when(reimbDaoMock.updateReimbursement(eq(1), eq(ReimbursementStatus.APPROVED), any(Timestamp.class), eq(null))).thenReturn(true);
		Mockito.when(reimbDaoMock.updateReimbursement(eq(1), eq(ReimbursementStatus.INVALID), any(Timestamp.class), eq(testUser))).thenReturn(true);
		
		// Vanilla test
		boolean updated = reimbService.updateReimbursement(1, "APPROVED", testUser);
		assertEquals(true, updated, "Failed vanilla test");
		
		// Negative id test
		updated = reimbService.updateReimbursement(-1, "APPROVED", testUser);
		assertEquals(false, updated, "Failed negative id test");
		
		// Zero id test
		updated = reimbService.updateReimbursement(0, "APPROVED", testUser);
		assertEquals(false, updated, "Failed zero id test");
		
		// Null user test
		updated = reimbService.updateReimbursement(1, "APPROVED", null);
		assertEquals(false, updated, "Failed negative id test");
		
		// Invalid status test
		updated = reimbService.updateReimbursement(1, "TESTING_GARBAGE", testUser);
		assertEquals(true, updated, "Failed negative id test");
	}
}
