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
        var hundredMetresRun = disciplineService.create(new DisciplineDTO(null, "100-meterløb", ResultType.TIME_IN_MILLISECONDS));
        var highJump = disciplineService.create(new DisciplineDTO(null, "Højdespring", ResultType.HEIGHT_IN_CENTIMETRES));
        var javelinThrow = disciplineService.create(new DisciplineDTO(null, "Spydkast", ResultType.DISTANCE_IN_CENTIMETRES));
        var longJump = disciplineService.create(new DisciplineDTO(null, "Længdespring", ResultType.LENGTH_IN_CENTIMETRES));
        var shotPut = disciplineService.create(new DisciplineDTO(null, "Kuglestød", ResultType.DISTANCE_IN_CENTIMETRES));
        var fourHundredMetresRun = disciplineService.create(new DisciplineDTO(null, "400-meterløb", ResultType.TIME_IN_MILLISECONDS));
        var discusThrow = disciplineService.create(new DisciplineDTO(null, "Diskoskast", ResultType.DISTANCE_IN_CENTIMETRES));
        var poleVault = disciplineService.create(new DisciplineDTO(null, "Stangspring", ResultType.HEIGHT_IN_CENTIMETRES));
        var tripleJump = disciplineService.create(new DisciplineDTO(null, "Trespring", ResultType.LENGTH_IN_CENTIMETRES));
        var hammerThrow = disciplineService.create(new DisciplineDTO(null, "Hammerkast", ResultType.DISTANCE_IN_CENTIMETRES));
        var eightHundredMetresRun = disciplineService.create(new DisciplineDTO(null, "800-meterløb", ResultType.TIME_IN_MILLISECONDS));
        var decathlon = disciplineService.create(new DisciplineDTO(null, "Decathlon", ResultType.POINTS));
        var heptathlon = disciplineService.create(new DisciplineDTO(null, "Heptathlon", ResultType.POINTS));
        var marathon = disciplineService.create(new DisciplineDTO(null, "Maraton", ResultType.TIME_IN_MILLISECONDS));

        // Seniors
        var anna = participantService.create(new ParticipantDTO(null, "Anna", false, LocalDate.of(1980, 1, 1), "Aalborg IF"));
        participantService.addDiscipline(anna.id(), hundredMetresRun.id());
        participantService.addDiscipline(anna.id(), fourHundredMetresRun.id());
        participantService.addDiscipline(anna.id(), eightHundredMetresRun.id());

        var benny = participantService.create(new ParticipantDTO(null, "Benny", true, LocalDate.of(1975, 5, 5), "Ballerup IF"));
        participantService.addDiscipline(benny.id(), highJump.id());
        participantService.addDiscipline(benny.id(), poleVault.id());
        participantService.addDiscipline(benny.id(), tripleJump.id());

        var clara = participantService.create(new ParticipantDTO(null, "Clara", false, LocalDate.of(1985, 12, 24), "Copenhagen IF"));
        participantService.addDiscipline(clara.id(), javelinThrow.id());
        participantService.addDiscipline(clara.id(), discusThrow.id());

        var dennis = participantService.create(new ParticipantDTO(null, "Dennis", true, LocalDate.of(1970, 3, 15), "DGI"));
        participantService.addDiscipline(dennis.id(), marathon.id());


        // Adults
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

        // Juniors
        var freja = participantService.create(new ParticipantDTO(null, "Freja", false, LocalDate.of(2004, 1, 20), "Frederiksberg IF"));
        participantService.addDiscipline(freja.id(), hundredMetresRun.id());
        participantService.addDiscipline(freja.id(), fourHundredMetresRun.id());
        participantService.addDiscipline(freja.id(), eightHundredMetresRun.id());

        var gert = participantService.create(new ParticipantDTO(null, "Gert", true, LocalDate.of(2005, 5, 10), "Glostrup AC"));
        participantService.addDiscipline(gert.id(), highJump.id());
        participantService.addDiscipline(gert.id(), poleVault.id());
        participantService.addDiscipline(gert.id(), tripleJump.id());

        var helle = participantService.create(new ParticipantDTO(null, "Helle", false, LocalDate.of(2006, 12, 24), "Helsingør IF"));
        participantService.addDiscipline(helle.id(), javelinThrow.id());
        participantService.addDiscipline(helle.id(), discusThrow.id());

        var ib = participantService.create(new ParticipantDTO(null, "Ib", true, LocalDate.of(2007, 3, 15), "Ishøj IF"));
        participantService.addDiscipline(ib.id(), marathon.id());

        // Youngsters
        var jens = participantService.create(new ParticipantDTO(null, "Jens", true, LocalDate.of(2012, 1, 20), "Jelling IF"));
        participantService.addDiscipline(jens.id(), hundredMetresRun.id());
        participantService.addDiscipline(jens.id(), fourHundredMetresRun.id());

        var kirsten = participantService.create(new ParticipantDTO(null, "Kirsten", false, LocalDate.of(2013, 5, 10), "Køge AC"));
        participantService.addDiscipline(kirsten.id(), highJump.id());
        participantService.addDiscipline(kirsten.id(), poleVault.id());

        var lars = participantService.create(new ParticipantDTO(null, "Lars", true, LocalDate.of(2012, 12, 24), "Lyngby IF"));
        participantService.addDiscipline(lars.id(), javelinThrow.id());
        participantService.addDiscipline(lars.id(), discusThrow.id());

        // Children
        var mette = participantService.create(new ParticipantDTO(null, "Mette", false, LocalDate.of(2016, 3, 15), "Middelfart IF"));
        participantService.addDiscipline(mette.id(), marathon.id());

        var niels = participantService.create(new ParticipantDTO(null, "Niels", true, LocalDate.of(2017, 1, 20), "Næstved IF"));
        participantService.addDiscipline(niels.id(), decathlon.id());
        participantService.addDiscipline(niels.id(), heptathlon.id());


        var annaHundredMetresResult = resultService.create(new ResultDTO(null, LocalDate.now().minusDays(1), 10500, ResultType.TIME_IN_MILLISECONDS, anna.id(), hundredMetresRun.id()));
        var annaFourHundredMetresResult = resultService.create(new ResultDTO(null, LocalDate.now().minusDays(2), 45500, ResultType.TIME_IN_MILLISECONDS, anna.id(), fourHundredMetresRun.id()));

        var bennyHighJumpResult = resultService.create(new ResultDTO(null, LocalDate.now().minusDays(1), 250, ResultType.HEIGHT_IN_CENTIMETRES, benny.id(), highJump.id()));
        var bennyPoleVaultResult = resultService.create(new ResultDTO(null, LocalDate.now().minusDays(2), 450, ResultType.HEIGHT_IN_CENTIMETRES, benny.id(), poleVault.id()));

        var claraJavelinThrowResult = resultService.create(new ResultDTO(null, LocalDate.now().minusDays(1), 5050, ResultType.DISTANCE_IN_CENTIMETRES, clara.id(), javelinThrow.id()));
        var claraDiscusThrowResult = resultService.create(new ResultDTO(null, LocalDate.now().minusDays(2), 4050, ResultType.DISTANCE_IN_CENTIMETRES, clara.id(), discusThrow.id()));

        var dennisMarathonResult = resultService.create(new ResultDTO(null, LocalDate.now().minusDays(1), 120*60*1000, ResultType.TIME_IN_MILLISECONDS, dennis.id(), marathon.id()));

        var andersHundredMetresResult = resultService.create(new ResultDTO(null, LocalDate.now().minusDays(1), 10500, ResultType.TIME_IN_MILLISECONDS, anders.id(), hundredMetresRun.id()));
        var andersFourHundredMetresResult = resultService.create(new ResultDTO(null, LocalDate.now().minusDays(2), 45500, ResultType.TIME_IN_MILLISECONDS, anders.id(), fourHundredMetresRun.id()));
        var andersEightHundredMetresResult = resultService.create(new ResultDTO(null, LocalDate.now().minusDays(3), 120500, ResultType.TIME_IN_MILLISECONDS, anders.id(), eightHundredMetresRun.id()));

        var benteHighJumpResult = resultService.create(new ResultDTO(null, LocalDate.now().minusDays(1), 150, ResultType.HEIGHT_IN_CENTIMETRES, bente.id(), highJump.id()));
        var bentePoleVaultResult = resultService.create(new ResultDTO(null, LocalDate.now().minusDays(2), 350, ResultType.HEIGHT_IN_CENTIMETRES, bente.id(), poleVault.id()));
        var benteTripleJumpResult = resultService.create(new ResultDTO(null, LocalDate.now().minusDays(3), 1250, ResultType.LENGTH_IN_CENTIMETRES, bente.id(), tripleJump.id()));
        var benteLongJumpResult = resultService.create(new ResultDTO(null, LocalDate.now().minusDays(4), 650, ResultType.LENGTH_IN_CENTIMETRES, bente.id(), longJump.id()));

        var clausJavelinThrowResult = resultService.create(new ResultDTO(null, LocalDate.now().minusDays(1), 5050, ResultType.DISTANCE_IN_CENTIMETRES, claus.id(), javelinThrow.id()));
        var clausDiscusThrowResult = resultService.create(new ResultDTO(null, LocalDate.now().minusDays(2), 4050, ResultType.DISTANCE_IN_CENTIMETRES, claus.id(), discusThrow.id()));
        var clausShotPutResult = resultService.create(new ResultDTO(null, LocalDate.now().minusDays(3), 1050, ResultType.DISTANCE_IN_CENTIMETRES, claus.id(), shotPut.id()));
        var clausHammerThrowResult = resultService.create(new ResultDTO(null, LocalDate.now().minusDays(4), 3050, ResultType.DISTANCE_IN_CENTIMETRES, claus.id(), hammerThrow.id()));

        var dortheMarathonResult = resultService.create(new ResultDTO(null, LocalDate.now().minusDays(1), 120*60*1000, ResultType.TIME_IN_MILLISECONDS, dorthe.id(), marathon.id()));

        var erikDecathlonResult = resultService.create(new ResultDTO(null, LocalDate.now().minusDays(1), 5000, ResultType.POINTS, erik.id(), decathlon.id()));
        var erikHeptathlonResult = resultService.create(new ResultDTO(null, LocalDate.now().minusDays(2), 4000, ResultType.POINTS, erik.id(), heptathlon.id()));
        var erikJavelinThrowResult = resultService.create(new ResultDTO(null, LocalDate.now().minusDays(3), 6050, ResultType.DISTANCE_IN_CENTIMETRES, erik.id(), javelinThrow.id()));
        var erikDiscusThrowResult = resultService.create(new ResultDTO(null, LocalDate.now().minusDays(4), 5050, ResultType.DISTANCE_IN_CENTIMETRES, erik.id(), discusThrow.id()));
        var erikShotPutResult = resultService.create(new ResultDTO(null, LocalDate.now().minusDays(5), 1550, ResultType.DISTANCE_IN_CENTIMETRES, erik.id(), shotPut.id()));

        var frejaHundredMetresResult = resultService.create(new ResultDTO(null, LocalDate.now().minusDays(1), 15500, ResultType.TIME_IN_MILLISECONDS, freja.id(), hundredMetresRun.id()));
        var frejaFourHundredMetresResult = resultService.create(new ResultDTO(null, LocalDate.now().minusDays(2), 65500, ResultType.TIME_IN_MILLISECONDS, freja.id(), fourHundredMetresRun.id()));
        var frejaEightHundredMetresResult = resultService.create(new ResultDTO(null, LocalDate.now().minusDays(3), 155500, ResultType.TIME_IN_MILLISECONDS, freja.id(), eightHundredMetresRun.id()));

        var gertHighJumpResult = resultService.create(new ResultDTO(null, LocalDate.now().minusDays(1), 250, ResultType.HEIGHT_IN_CENTIMETRES, gert.id(), highJump.id()));
        var gertPoleVaultResult = resultService.create(new ResultDTO(null, LocalDate.now().minusDays(2), 450, ResultType.HEIGHT_IN_CENTIMETRES, gert.id(), poleVault.id()));
        var gertTripleJumpResult = resultService.create(new ResultDTO(null, LocalDate.now().minusDays(3), 1350, ResultType.LENGTH_IN_CENTIMETRES, gert.id(), tripleJump.id()));

        var helleJavelinThrowResult = resultService.create(new ResultDTO(null, LocalDate.now().minusDays(1), 6050, ResultType.DISTANCE_IN_CENTIMETRES, helle.id(), javelinThrow.id()));
        var helleDiscusThrowResult = resultService.create(new ResultDTO(null, LocalDate.now().minusDays(2), 5050, ResultType.DISTANCE_IN_CENTIMETRES, helle.id(), discusThrow.id()));

        var ibMarathonResult = resultService.create(new ResultDTO(null, LocalDate.now().minusDays(1), 120*60*1000, ResultType.TIME_IN_MILLISECONDS, ib.id(), marathon.id()));

        var jensHundredMetresResult = resultService.create(new ResultDTO(null, LocalDate.now().minusDays(1), 20500, ResultType.TIME_IN_MILLISECONDS, jens.id(), hundredMetresRun.id()));
        var jensFourHundredMetresResult = resultService.create(new ResultDTO(null, LocalDate.now().minusDays(2), 85500, ResultType.TIME_IN_MILLISECONDS, jens.id(), fourHundredMetresRun.id()));

        var kirstenHighJumpResult = resultService.create(new ResultDTO(null, LocalDate.now().minusDays(1), 350, ResultType.HEIGHT_IN_CENTIMETRES, kirsten.id(), highJump.id()));
        var kirstenPoleVaultResult = resultService.create(new ResultDTO(null, LocalDate.now().minusDays(2), 550, ResultType.HEIGHT_IN_CENTIMETRES, kirsten.id(), poleVault.id()));

        var larsJavelinThrowResult = resultService.create(new ResultDTO(null, LocalDate.now().minusDays(1), 7050, ResultType.DISTANCE_IN_CENTIMETRES, lars.id(), javelinThrow.id()));
        var larsDiscusThrowResult = resultService.create(new ResultDTO(null, LocalDate.now().minusDays(2), 6050, ResultType.DISTANCE_IN_CENTIMETRES, lars.id(), discusThrow.id()));

        var metteMarathonResult = resultService.create(new ResultDTO(null, LocalDate.now().minusDays(1), 120*60*1000, ResultType.TIME_IN_MILLISECONDS, mette.id(), marathon.id()));

        var nielsDecathlonResult = resultService.create(new ResultDTO(null, LocalDate.now().minusDays(1), 6000, ResultType.POINTS, niels.id(), decathlon.id()));
        var nielsHeptathlonResult = resultService.create(new ResultDTO(null, LocalDate.now().minusDays(2), 5000, ResultType.POINTS, niels.id(), heptathlon.id()));
    }

}
