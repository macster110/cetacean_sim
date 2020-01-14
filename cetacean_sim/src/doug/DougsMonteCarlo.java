package doug;

import java.util.Arrays;

import animal.DefaultBeamProfiles;
import animal.SimpleOdontocete;
import layout.animal.BeamProfile;
import propogation.Propogation;
import propogation.SimplePropogation;
import simulation.NormalSimVariable;
import utils.CetSimUtils;
import utils.Hist3;

public class DougsMonteCarlo {

	private double noiseBackground = 107;
	private double noiseSTD = 8;

	private double detThreshold = 22+6;
	
	private double sourceLevel = 191;

	private Propogation propogation = new SimplePropogation(20, 0.04);
	
	private NormalSimVariable noise = new NormalSimVariable("Noise", noiseBackground, noiseSTD);


	public static double[][] meygenPhones =
		{
				{3.54700000000000,	3.54700000000000,	-12.1220000000000},
				{3.54700000000000,	-3.54700000000000,	-12.1220000000000},
				{-6.97500000000000,	0.0480000000000000,	-12.6900000000000},
		};
//	public static double[][] meygenPhones =
//		{
//				{5,	0,	-12},
//				{0, 0,	-12},
//				{-5,	0,	-12},
//		};
	public static void main(String[] args) {
		DougsMonteCarlo dmc = new DougsMonteCarlo();
		dmc.run();
	}

	private void run() {
		SimpleOdontocete animal = new SimpleOdontocete();
		animal.setBeamProfile(new DefaultBeamProfiles().getDefaultBeams().get(0));
		animal.sourceLevel = new NormalSimVariable("Source Level", sourceLevel, 0);
		
		
//		if ( 1> 0) {
//			printBeamProfile(animal.getBeamProfile());
//			return;
//		}
		
		double[] rangeBins = Hist3.binEdges(-50, 50, 25);
		double[] depthRange = {-20, 0};
		int nTrial = 50000;
		MeygenPositionGenerator xyzGenerator = new MeygenPositionGenerator(null);
		double[][] results = new double[rangeBins.length-1][4];

		for (int l = 0; l < 4; l++) {
			xyzGenerator.setCurrentLoc(l);
			for (int i = 0; i < rangeBins.length-1; i++) {
				double[] rr = Arrays.copyOfRange(rangeBins, i, i+2);
				//			 rpg = new RangeDepthPositionGenerator(rr, depthRange);
				//			rpg = new MeygenDiscPosition(rr, 0, 9);
				xyzGenerator.setxRange(rr);
				double p = getDetProb(animal, xyzGenerator, nTrial);
				System.out.printf("%3.0f < r < %3.0f, p = %6.4f\n", rr[0], rr[1], p);
				results[i][l] = p;
			}
		}

		System.out.println("Done\n\n\n\n");
		for (int i = 0; i < rangeBins.length-1; i++) {
			
			System.out.printf("\n%3.1f", rangeBins[i]);
			for (int l = 0; l < 4; l++) {
				System.out.printf(",%3.5f", results[i][l]);
			}
		}
	}

	private double getDetProb(SimpleOdontocete animal, RandomPositionGenerator rpg, int nTrial) {
	
		int nLocalised = 0;
	
		for (int i = 0; i < nTrial; i++) {
			boolean flipPhones = Math.random() >= 0.5;
	
			double[] animalPos = rpg.getNextXYZ();
	
			double vertAngle = animal.getVertAngle(animalPos[2]-27).getNextRandom();
//			vertAngle = animal.getVertAngle(animalPos[2]-27,  )
			vertAngle = 0;
			double horzAngle = animal.horzAngle.getNextRandom();		
			double[] animalAngles = new double[] {horzAngle, vertAngle};
			int nPhone = meygenPhones.length;
			double sourceLevel = animal.sourceLevel.getNextRandom();
			int phonesHit = 0;
			double threshold = noise.getNextRandom() + detThreshold;
			for (int p = 0; p < nPhone; p++) {
				double[] phonePos = meygenPhones[p].clone();
				if (flipPhones) {
					phonePos[0] = -phonePos[0];
				}
				double transloss = CetSimUtils.tranmissionTotalLoss(phonePos, 
						animalPos, animalAngles, animal.beamSurface, propogation); 
				double rl = sourceLevel+transloss;
				if (rl > threshold) {
					phonesHit++;
				}
			}
			if (phonesHit >= 2) {
				nLocalised++;
			}
		}
		return (double) nLocalised / (double) nTrial;
	}

	private void printBeamProfile(BeamProfile beamProfile) {

		double[] ang1Bins = Hist3.binEdges(-Math.PI*2, Math.PI*2, 90);
		double[] ang2Bins = Hist3.binEdges(-Math.PI, Math.PI, 45);
		System.out.println("Horizontal angle");
		for (int i = 0; i < ang1Bins.length; i++) {
			System.out.printf("%3.2f", ang1Bins[i]*180/Math.PI);
			if (i < ang1Bins.length-1) {
				System.out.printf(",");
			}
			else {
				System.out.printf("\n\n");
			}
		}
		System.out.println("Vertical angle");
		for (int i = 0; i < ang2Bins.length; i++) {
			System.out.printf("%3.2f", ang2Bins[i]*180/Math.PI);
			if (i < ang2Bins.length-1) {
				System.out.printf(",");
			}
			else {
				System.out.printf("\n\n");
			}
		}
		
		System.out.println("Beam Profile");
		for (int i1 = 0; i1 < ang1Bins.length; i1++) {
			for (int i2 = 0; i2 < ang2Bins.length; i2++) {
				double tl = beamProfile.getTL(ang1Bins[i1], ang2Bins[i2]);
				if (i2 == 0) {
					System.out.printf("%3.5f", tl);
				}
				else {
					System.out.printf(",%3.5f", tl);
				}
				
			}
			System.out.printf("\n");
		}
		
	}

}
