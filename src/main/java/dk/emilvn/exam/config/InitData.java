package dk.emilvn.exam.config;

import dk.emilvn.exam.discipline.DisciplineDTO;
import dk.emilvn.exam.discipline.DisciplineRepository;
import dk.emilvn.exam.discipline.DisciplineService;
import dk.emilvn.exam.participant.ParticipantDTO;
import dk.emilvn.exam.participant.ParticipantRepository;
import dk.emilvn.exam.participant.ParticipantService;
import dk.emilvn.exam.result.ResultDTO;
import dk.emilvn.exam.result.ResultRepository;
import dk.emilvn.exam.result.ResultService;
import dk.emilvn.exam.result.ResultType;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@Profile("!test")
public class InitData implements CommandLineRunner {

    private final ParticipantService participantService;
    private final DisciplineService disciplineService;
    private final ResultService resultService;
    private final ParticipantRepository participantRepository;
    private final DisciplineRepository disciplineRepository;
    private final ResultRepository resultRepository;

    public InitData(ParticipantService participantService, DisciplineService disciplineService, ResultService resultService, ParticipantRepository participantRepository, DisciplineRepository disciplineRepository, ResultRepository resultRepository) {
        this.participantService = participantService;
        this.disciplineService = disciplineService;
        this.resultService = resultService;
        this.participantRepository = participantRepository;
        this.disciplineRepository = disciplineRepository;
        this.resultRepository = resultRepository;

    }

    @Override
    public void run(String... args) throws Exception {
        if(participantRepository.count() == 0 && disciplineRepository.count() == 0 && resultRepository.count() == 0) {
            init();
        }
    }

    private void init() {
        var hundredMetresRun = disciplineService.create(new DisciplineDTO(null, "100-meterløb", ResultType.TIME_IN_SECONDS));
        var highJump = disciplineService.create(new DisciplineDTO(null, "Højdespring", ResultType.HEIGHT_IN_METRES));
        var javelinThrow = disciplineService.create(new DisciplineDTO(null, "Spydkast", ResultType.DISTANCE_IN_METRES));
        var longJump = disciplineService.create(new DisciplineDTO(null, "Længdespring", ResultType.LENGTH_IN_METRES));
        var shotPut = disciplineService.create(new DisciplineDTO(null, "Kuglestød", ResultType.DISTANCE_IN_METRES));
        var fourHundredMetresRun = disciplineService.create(new DisciplineDTO(null, "400-meterløb", ResultType.TIME_IN_SECONDS));
        var discusThrow = disciplineService.create(new DisciplineDTO(null, "Diskoskast", ResultType.DISTANCE_IN_METRES));
        var poleVault = disciplineService.create(new DisciplineDTO(null, "Stangspring", ResultType.HEIGHT_IN_METRES));
        var tripleJump = disciplineService.create(new DisciplineDTO(null, "Trespring", ResultType.LENGTH_IN_METRES));
        var hammerThrow = disciplineService.create(new DisciplineDTO(null, "Hammerkast", ResultType.DISTANCE_IN_METRES));
        var eightHundredMetresRun = disciplineService.create(new DisciplineDTO(null, "800-meterløb", ResultType.TIME_IN_SECONDS));
        var decathlon = disciplineService.create(new DisciplineDTO(null, "Decathlon", ResultType.POINTS));
        var heptathlon = disciplineService.create(new DisciplineDTO(null, "Heptathlon", ResultType.POINTS));
        var marathon = disciplineService.create(new DisciplineDTO(null, "Maraton", ResultType.TIME_IN_SECONDS));

        var anders = participantService.create(new ParticipantDTO(null, "Anders", true, LocalDate.of(1990, 1, 20), "Aarhus 1900"));
        participantService.addDiscipline(anders.id(), hundredMetresRun.id());
        participantService.addDiscipline(anders.id(), fourHundredMetresRun.id());
        participantService.addDiscipline(anders.id(), eightHundredMetresRun.id());

        var bente = participantService.create(new ParticipantDTO(null, "Bente", false, LocalDate.of(1995, 5, 10), "Bagsværd AC"));
        participantService.addDiscipline(bente.id(), highJump.id());
        participantService.addDiscipline(bente.id(), poleVault.id());
        participantService.addDiscipline(bente.id(), tripleJump.id());
        participantService.addDiscipline(bente.id(), longJump.id());

        var claus = participantService.create(new ParticipantDTO(null, "Claus", true, LocalDate.of(1992, 12, 24), "Copenhagen IF"));
        participantService.addDiscipline(claus.id(), javelinThrow.id());
        participantService.addDiscipline(claus.id(), discusThrow.id());
        participantService.addDiscipline(claus.id(), shotPut.id());
        participantService.addDiscipline(claus.id(), hammerThrow.id());

        var dorthe = participantService.create(new ParticipantDTO(null, "Dorthe", false, LocalDate.of(1998, 3, 15), "DGI"));
        participantService.addDiscipline(dorthe.id(), marathon.id());

        var erik = participantService.create(new ParticipantDTO(null, "Erik", true, LocalDate.of(1993, 7, 5), "Esbjerg IF"));

        participantService.addDiscipline(erik.id(), decathlon.id());
        participantService.addDiscipline(erik.id(), heptathlon.id());
        participantService.addDiscipline(erik.id(), javelinThrow.id());
        participantService.addDiscipline(erik.id(), discusThrow.id());
        participantService.addDiscipline(erik.id(), shotPut.id());


        var andersHundredMetresResult = resultService.create(new ResultDTO(null, LocalDate.now().minusDays(1), 10.5, ResultType.TIME_IN_SECONDS, anders.id(), hundredMetresRun.id()));
        var andersFourHundredMetresResult = resultService.create(new ResultDTO(null, LocalDate.now().minusDays(2), 45.5, ResultType.TIME_IN_SECONDS, anders.id(), fourHundredMetresRun.id()));
        var andersEightHundredMetresResult = resultService.create(new ResultDTO(null, LocalDate.now().minusDays(3), 120.5, ResultType.TIME_IN_SECONDS, anders.id(), eightHundredMetresRun.id()));

        var benteHighJumpResult = resultService.create(new ResultDTO(null, LocalDate.now().minusDays(4), 1.5, ResultType.HEIGHT_IN_METRES, bente.id(), highJump.id()));
        var bentePoleVaultResult = resultService.create(new ResultDTO(null, LocalDate.now().minusDays(5), 3.5, ResultType.HEIGHT_IN_METRES, bente.id(), poleVault.id()));
        var benteTripleJumpResult = resultService.create(new ResultDTO(null, LocalDate.now().minusDays(6), 12.5, ResultType.LENGTH_IN_METRES, bente.id(), tripleJump.id()));
        var benteLongJumpResult = resultService.create(new ResultDTO(null, LocalDate.now().minusDays(7), 6.5, ResultType.LENGTH_IN_METRES, bente.id(), longJump.id()));

        var clausJavelinThrowResult = resultService.create(new ResultDTO(null, LocalDate.now().minusDays(8), 50.5, ResultType.DISTANCE_IN_METRES, claus.id(), javelinThrow.id()));
        var clausDiscusThrowResult = resultService.create(new ResultDTO(null, LocalDate.now().minusDays(9), 40.5, ResultType.DISTANCE_IN_METRES, claus.id(), discusThrow.id()));
        var clausShotPutResult = resultService.create(new ResultDTO(null, LocalDate.now().minusDays(10), 10.5, ResultType.DISTANCE_IN_METRES, claus.id(), shotPut.id()));
        var clausHammerThrowResult = resultService.create(new ResultDTO(null, LocalDate.now().minusDays(11), 30.5, ResultType.DISTANCE_IN_METRES, claus.id(), hammerThrow.id()));

        var dortheMarathonResult = resultService.create(new ResultDTO(null, LocalDate.now().minusDays(12), 120.5*60, ResultType.TIME_IN_SECONDS, dorthe.id(), marathon.id()));

        var erikDecathlonResult = resultService.create(new ResultDTO(null, LocalDate.now().minusDays(13), 5000.5, ResultType.POINTS, erik.id(), decathlon.id()));
        var erikHeptathlonResult = resultService.create(new ResultDTO(null, LocalDate.now().minusDays(14), 4000.5, ResultType.POINTS, erik.id(), heptathlon.id()));
        var erikJavelinThrowResult = resultService.create(new ResultDTO(null, LocalDate.now().minusDays(15), 60.5, ResultType.DISTANCE_IN_METRES, erik.id(), javelinThrow.id()));
        var erikDiscusThrowResult = resultService.create(new ResultDTO(null, LocalDate.now().minusDays(16), 50.5, ResultType.DISTANCE_IN_METRES, erik.id(), discusThrow.id()));
        var erikShotPutResult = resultService.create(new ResultDTO(null, LocalDate.now().minusDays(17), 15.5, ResultType.DISTANCE_IN_METRES, erik.id(), shotPut.id()));
    }

}
