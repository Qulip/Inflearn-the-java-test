package me.whiteship.inflearnthejavatest.study;

import static org.junit.jupiter.api.Assertions.*;
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

	@Test
	void createNewStudy(@Mock MemberService memberService,
		@Mock StudyRepository studyRepository) {

		// MemberService memberService = mock(MemberService.class);
		// StudyRepository studyRepository = mock(StudyRepository.class);

		// Optional<Member> optional = memberService.findById(1L);
		// memberService.validate(2L);

		StudyService studyService = new StudyService(memberService, studyRepository);
		assertNotNull(studyService);

		Member member = new Member();
		member.setId(1L);
		member.setEmail("keesun@email.com");

		when(memberService.findById(any()))
			.thenReturn(Optional.of(member))
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
}