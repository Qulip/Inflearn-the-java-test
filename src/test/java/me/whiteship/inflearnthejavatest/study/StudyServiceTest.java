package me.whiteship.inflearnthejavatest.study;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import me.whiteship.inflearnthejavatest.domain.Member;
import me.whiteship.inflearnthejavatest.domain.Study;
import me.whiteship.inflearnthejavatest.member.MemberService;

@ExtendWith(MockitoExtension.class)
class StudyServiceTest {
	@Mock
	MemberService memberService;
	@Mock
	StudyRepository studyRepository;

	@Test
	void createNewStudy() {

		// MemberService memberService = mock(MemberService.class);
		// StudyRepository studyRepository = mock(StudyRepository.class);

		// Optional<Member> optional = memberService.findById(1L);
		// memberService.validate(2L);

		StudyService studyService = new StudyService(memberService, studyRepository);
		assertNotNull(studyService);

		Member member = new Member();
		member.setId(1L);
		member.setEmail("keesun@email.com");

		when(memberService.findById(any())).thenReturn(Optional.of(member))
			.thenThrow(new RuntimeException())
			.thenReturn(Optional.empty());

		Optional<Member> byId = memberService.findById(1L);
		assertEquals("keesun@email.com", byId.get().getEmail());
		assertThrows(RuntimeException.class, () -> {
			memberService.findById(2L);
		});
		assertEquals(Optional.empty(), memberService.findById(3L));

		// Study study = new Study(10, "java");
		//
		// assertEquals("keesun@email.com", memberService.findById(1L).get().getEmail());
		// assertEquals("keesun@email.com", memberService.findById(2L).get().getEmail());
		//
		// doThrow(new IllegalArgumentException()).when(memberService).validate(1L);
		//
		// assertThrows(IllegalArgumentException.class, () -> {
		// 	memberService.validate(1L);
		// });
		//
		// memberService.validate(2L);
	}

	@Test
	void testNewStudy() {

		Study study = new Study(10, "테스트");

		Member member = new Member();
		// when(memberService.findById(1L)).thenReturn(Optional.of(member));
		given(memberService.findById(1L)).willReturn(Optional.of(member));
		// when(studyRepository.save(study)).thenReturn(study);
		given(studyRepository.save(study)).willReturn(study);


		StudyService studyService = new StudyService(memberService, studyRepository);
		studyService.createNewStudy(1L, study);

		assertNotNull(study.getOwner());
		assertEquals(member, study.getOwner());

		// verify(memberService, times(1)).notify(study);
		then(memberService).should(times(1)).notify(study);
		// verify(memberService, never()).validate(any());
		then(memberService).shouldHaveNoInteractions();
	}

	@Test
	void openStudy() {
		//Given
		StudyService studyService = new StudyService(memberService, studyRepository);
		Study study = new Study(10, "테스트");
		assertNull(study.getOpenedDateTime());

		given(studyRepository.save(study)).willReturn(study);

		//When
		studyService.openStudy(study);

		//Then
		assertEquals(StudyStatus.OPENED, study.getStatus());
		assertNotNull(study.getOpenedDateTime());
		then(memberService).should().notify();
	}
}