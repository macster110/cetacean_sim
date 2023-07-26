package simulation.probdettrack;

import java.util.ArrayList;

import animal.AnimalModel;
import animal.AnimalVocalisations;
import reciever.HydrophoneArray;
import simulation.StatusListener;
import utils.CetSimUtils;
import utils.Hist3;
import utils.SurfaceData;
import utils.SurfaceUtils;

/**
 * Calculates the whether clicks from an animal along a defined track were
 * detected or not on a given reciever.
 * 
 * @author Jamie Macaulay
 *
 */
public class ProbDetTrack {

	/**
	 * The settings for the simulation
	 */
	public ProbDetTrackSettings probTrackSettings;

	/**
	 * The results.
	 */
	public ArrayList<RecievedInfo> recInfo;

	/**
	 * The y edges of the histogram
	 */
	private double[] yBinEdges;

	/**
	 * The x edges of the histogram.
	 */
	private double[] xBinEdges;

	/**
	 * The probability of detection results for each track.
	 */
	private ArrayList<Hist3> probDetResults = new ArrayList<Hist3>();

	/**
	 * Histogram of track effortv- i.e. how many clicks were in each bin.
	 */
	private ArrayList<Hist3> probDetEffort = new ArrayList<Hist3>();

	/**
	 * List of status listeners for updates to the Monte Carlo Simulation.
	 */
	private ArrayList<StatusListener> statusListeners = new ArrayList<StatusListener>();

	/**
	 * Get the simulation ready to run
	 */
	public void setupProbTrack(ProbDetTrackSettings simSettings) {
		// print the settings.
		simSettings.printSettings();

		System.out.println("DEPTH BIN: " + simSettings.minHeight + " " + simSettings.numDepthBins);
		this.xBinEdges = Hist3.binEdges(0, simSettings.maxRange, simSettings.numRangeBins);
		this.yBinEdges = Hist3.binEdges(simSettings.minHeight, 0, simSettings.numDepthBins);

		System.out.println("Y bin edges: ");
		for (int i = 0; i < yBinEdges.length; i++) {
			System.out.print(String.format("%.1f ", yBinEdges[i]));
		}
	}

	/**
	 * Run the simulation to determine the probability of detecting a single click.
	 */
	public void runProbTrack(ProbDetTrackSettings simSettings) {

		// easy access to the recievers object.
		HydrophoneArray recievers = simSettings.recievers;

		// System.out.println("Is this cancelled? " + this.cancel);
		AnimalModel animal = simSettings.animal;

		notifyStatusListeners(StatusListener.SIM_STARTED, 0, 0);

		double[] recieverPos;
		// iterate through each animal
		// System.out.println("Number of animals: " +
		// animal.getNumberOfAnimals().intValue());

		for (int n = 0; n < animal.getNumberOfAnimals().intValue(); n++) {

			notifyStatusListeners(StatusListener.SIM_RUNNING, n, 0);

			// get the animal vocalisation objects.
			AnimalVocalisations animalVocalisations = animal.getVocSeries(n);

			// get the track points for the vocalisation times.
			double[][] trackXYZ = animal.getTrack(n).getTrackPoints(animalVocalisations.getVocTimes());

			double[] depthMinMax = CetSimUtils.getMinAndMax(trackXYZ[2]);
			System.out.println("Depth minmax: " + depthMinMax[0] + "  " + depthMinMax[1]);

			// get the track points for the vocalisation times.
			double[][] trackOrient = animal.getTrack(n).getTrackAngles(animalVocalisations.getVocTimes());

			SurfaceData beamSurface = SurfaceUtils.generateSurface(animal.getBeamProfileTL().getRawBeamMeasurments());

			// the received info array.
			System.out.println("Number of voc: " + animalVocalisations.getVocTimes().length);
			System.out.println("Number of recievers: " + recievers.getArrayXYZ().length);

			// iterate through each click
			double[] animalPos;
			double[] animalAngle;
			double transloss;
			double distance;
			double recievedLevel;

			Hist3 pDet = new Hist3(this.xBinEdges, this.yBinEdges);
			Hist3 effortDet = new Hist3(this.xBinEdges, this.yBinEdges);

			for (int i = 0; i < animalVocalisations.getVocTimes().length; i++) {
				// for (int i = 0; i<100000 ; i++) {

				// iterate through each receiver and calculate the received level on each

				// create the track info array. This holds a received level for each reciver.
				ArrayList<RecievedInfo> trackRecInfo = new ArrayList<RecievedInfo>(recievers.getArrayXYZ().length);

				for (int j = 0; j < recievers.getArrayXYZ().length; j++) {
					recieverPos = recievers.getArrayXYZ()[j];

					// create the track points.
					animalPos = new double[] { trackXYZ[0][i], trackXYZ[1][i], trackXYZ[2][i] };

					if (simSettings.useRoll) {
						animalAngle = new double[] { trackOrient[0][i], trackOrient[1][i], trackOrient[2][i] };
						if (Double.isNaN(animalAngle[2])) {
							System.out.println("The animal orientation is NaN: " + j); 
							animalAngle[2] = 0; //dunno why this sometimes happens - issue with sensor fusion algorithm
						}
					} else {
						animalAngle = new double[] { trackOrient[0][i], trackOrient[1][i] };
					}
					
		

					// the 3D distance
					distance = CetSimUtils.distance2D(animalPos, recieverPos);

					if (distance > simSettings.maxRange) {
						// this is very important - in distance sampling we consider a specified area -
						// if the distance is
						// greater than the maximum range then we are outside the area and the result
						// should not be counted
						// at all.
						// trackRecInfo.add(new RecievedInfo(noDetRL, (float) distance, (float)
						// trackXYZ[2][i], j));
						continue;
					}

					// if (i<100) {
					// System.out.println(String.format("Animal Pos: %d xyz: %.2f %.2f %.2f meters"
					// , i , trackXYZ[0][i],trackXYZ[1][i] ,trackXYZ[2][i]));
					// System.out.println(String.format("Animal Angle: %d angs: %.2f %.2f %.2f RAD
					// %d", i , animalAngle[0] , animalAngle[1], animalAngle[2],
					// animalAngle.length));
					// System.out.println(String.format("Reciever Pos: %d xyz: %.2f %.2f %.2f meters
					// distance: %.0f meters", i, recieverPos[0], recieverPos[1], recieverPos[2],
					// distance));
					// }
					// the transmission loss.
					transloss = CetSimUtils.tranmissionTotalLoss(recieverPos, animalPos, animalAngle, beamSurface,
							simSettings.propogation);

					recievedLevel = animalVocalisations.getVocAmplitudes()[i] + transloss;

					// if (distance<1000) {
					// System.out.println(String.format("Received level: %.1f distance %.1f source
					// level %.1f TL: %.1f ", recievedLevel, distance,
					// animalVocalisations.getVocAmplitudes()[i], transloss));
					// }

					// add the results to an array
					trackRecInfo
							.add(new RecievedInfo((float) recievedLevel, (float) distance, (float) trackXYZ[2][i], j));

					// if (recievedLevel>(simSettings.noise + simSettings.snrThreshold)) {
					// System.out.println(String.format("We have a detection! %.1f ",
					// recievedLevel));
					// }
					// notifyStatusListeners(StatusListener.SIM_RUNNING, n , nn/(double)
					// animalVocalisations.getVocTimes().length*recievers.getArrayXYZ().length);
				}

				if (i % 1000 == 0) {
					notifyStatusListeners(StatusListener.SIM_RUNNING, n,
							i / (double) animalVocalisations.getVocTimes().length);
				}

				// now must add the results to the histogram
				pDet.		addToHist(trackRecInfo, 1.0, simSettings.noise + simSettings.snrThreshold);
				effortDet.	addToHist(trackRecInfo, null, Double.NEGATIVE_INFINITY); // will always be greater
			}

			// now have a giant array of distances and received levels.

			probDetResults.add(pDet);
			probDetEffort.add(effortDet);

		}

		notifyStatusListeners(StatusListener.SIM_FINIHSED, animal.getNumberOfAnimals().intValue(), 1);

	}

	
	/**
	 * Run the simulation for snapshots i.e. instead of determining the probability
	 * per clicks, it determines the probability per snapshot time bin.
	 */
	public void runProbTrackSnap(ProbDetTrackSettings simSettings) {

		// easy access to the recievers object.
		HydrophoneArray recievers = simSettings.recievers;

		// System.out.println("Is this cancelled? " + this.cancel);
		AnimalModel animal = simSettings.animal;

		notifyStatusListeners(StatusListener.SIM_STARTED, 0, 0);

		double[] recieverPos;
		// iterate through each animal
		// System.out.println("Number of animals: " +
		// animal.getNumberOfAnimals().intValue());

		for (int n = 0; n < animal.getNumberOfAnimals().intValue(); n++) {

			notifyStatusListeners(StatusListener.SIM_RUNNING, n, 0);

			// get the animal vocalisation objects.
			AnimalVocalisations animalVocalisations = animal.getVocSeries(n);

			// get the track points for the vocalisation times.
			double[][] trackXYZ = animal.getTrack(n).getTrackPoints(animalVocalisations.getVocTimes());
			
			double[] depthMinMax = CetSimUtils.getMinAndMax(trackXYZ[2]);
			System.out.println("Depth minmax: " + depthMinMax[0] + "  " + depthMinMax[1]);

			// get the track points for the vocalisation times.
			double[][] trackOrient = animal.getTrack(n).getTrackAngles(animalVocalisations.getVocTimes());

			// crate the beam surface.
			SurfaceData beamSurface = SurfaceUtils.generateSurface(animal.getBeamProfileTL().getRawBeamMeasurments());

			// the received info array.
			System.out.println("Number of voc: " + animalVocalisations.getVocTimes().length);
			System.out.println("Number of recievers: " + recievers.getArrayXYZ().length);

			// generate the time bins
			double[] timeLims = new double[2]; 
			if (simSettings.snapTimeLims==null) {
				//use the first and last vocalisation - may be inaccurate if animals do not vocalise consistently. 
				timeLims[0] = animalVocalisations.getVocTimes()[0];
				timeLims[1] = animalVocalisations.getVocTimes()[animalVocalisations.getVocTimes().length - 1]; 
			}
			else {
				timeLims = simSettings.snapTimeLims; 
			}
			
			System.out.println("Time lims: " + timeLims[0] + "  " + timeLims[1]);
			
			double[][] timeBins = getTimeBins(timeLims[0],timeLims[1],simSettings.snapTime);
			
			double[] timeBinsStart = new double[timeBins.length];
			for (int i =0; i<timeBinsStart.length; i++) {
				timeBinsStart[i]=timeBins[i][0]; 
			}
			
			double[][] trackBinXYZ = animal.getTrack(n).getTrackPoints(timeBinsStart);
			
			
			
			System.out.println("Time bins start: " + timeBins[0][0]);
			for (int xx = 0; xx<trackBinXYZ[0].length; xx++) {
				if (Double.isNaN(trackBinXYZ[0][xx])) {
					System.out.println("Is nan at " + xx + " for time: " + timeBins[xx][0]);
				}
			}


			// iterate through each click
			double[] animalPos;
			double[] animalAngle;
			double transloss;
			double distance;
			double recievedLevel;
			float noDetRL = 0; // definately won't be detected
			RecievedInfo recInfo;

			Hist3 pDet = new Hist3(this.xBinEdges, this.yBinEdges);
			Hist3 effortDet = new Hist3(this.xBinEdges, this.yBinEdges);

			for (int k = 0; k < timeBins.length; k++) {

				// get all vocalisation in the time bin
				ArrayList<Double> vocTimes = new ArrayList<Double>();
				ArrayList<Double> vocAmplitudes = new ArrayList<Double>();
				ArrayList<double[]> trackPoints = new ArrayList<double[]>();
				ArrayList<double[]> trackOrients = new ArrayList<double[]>();

				for (int kk = 0; kk < animalVocalisations.getVocTimes().length; kk++) {
					if (animalVocalisations.getVocTimes()[kk] >= timeBins[k][0]
							&& animalVocalisations.getVocTimes()[kk] < timeBins[k][1]) {
						vocTimes.add(animalVocalisations.getVocTimes()[kk]);
						vocAmplitudes.add(animalVocalisations.getVocAmplitudes()[kk]);
						trackPoints.add(new double[] { trackXYZ[0][kk], trackXYZ[1][kk], trackXYZ[2][kk] });
						if (simSettings.useRoll) {
							trackOrients
									.add(new double[] {trackOrient[0][kk], trackOrient[1][kk], trackOrient[2][kk] });
						} else {
							trackOrients.add(new double[] { trackOrient[0][kk], trackOrient[1][kk] });
						}
					}
				}
				
				
				// for (int i = 0; i<100000 ; i++) {

				// iterate through each receiver and calculate the received level on each

				// create the track info array. This holds a received level for each reciver.
				ArrayList<RecievedInfo> trackRecInfo = new ArrayList<RecievedInfo>(recievers.getArrayXYZ().length);

				// convert to a primitive
				double[] vocTimesD = vocTimes.stream().mapToDouble(d -> d).toArray();

				// double[] vocTimesD = (double[]) vocTimes.toArray());
				/**
				 * We iterate through all recievers.
				 */
				int count = 0; 
				int sum = 0; //the total number of detection made
				for (int j = 0; j < recievers.getArrayXYZ().length; j++) {

					recieverPos = recievers.getArrayXYZ()[j];

					if (vocTimes.size()==0) {
						// the 3D distance
						distance = CetSimUtils.distance2D(new double[] {trackBinXYZ[0][k], trackBinXYZ[1][k], trackBinXYZ[2][k] }, recieverPos);
						if (trackRecInfo.size()>430) {
								System.out.println(String.format("%.2f trackx %.2f",distance, trackBinXYZ[0][k]));
						}

						recInfo = new RecievedInfo(-1f, (float) distance, (float) trackBinXYZ[2][k], j);
						count++; 
					} 
					else {
						// hold the results for a sensor
						double[] recievedLevels = new double[vocTimes.size()];
						double[] distances = new double[vocTimes.size()];
						double[] heights = new double[vocTimes.size()];

						for (int i = 0; i < vocTimes.size(); i++) {

							// create the track points.
							animalPos = trackPoints.get(i);


							//animal angle
							animalAngle = trackOrients.get(i);

							// the 3D distance
							distance = CetSimUtils.distance2D(animalPos, recieverPos);
		

							if (distance > simSettings.maxRange) {
								// don't bother with the calculation - just set to -1;
								recievedLevels[i] = -1;
								distances[i] = distance;
								heights[i] = recieverPos[2];
								continue;
							}

							if (animalAngle.length>2 &&  Double.isNaN(animalAngle[2])) {
								System.out.println("The animal orientation is NaN: " + j); 
								animalAngle[2] = 0; //dunno why this sometimes happens - issue with sensor fusion algorithm
							}

							//						System.out.println("animalPos: " + animalPos[0] + "  " + animalPos[1] + "  " +animalPos[2]); 
							//						System.out.println("animalAngle: " + animalAngle[0] + "  " + animalAngle[1] + "  " +animalAngle[2]); 

							// the transmission loss.
							transloss = CetSimUtils.tranmissionTotalLoss(recieverPos, animalPos, animalAngle, beamSurface,
									simSettings.propogation);

							recievedLevel =vocAmplitudes.get(i)+ transloss;

							recievedLevels[i] = recievedLevel;
							distances[i] = distance;
							heights[i] = animalPos[2];

							// notifyStatusListeners(StatusListener.SIM_RUNNING, n , nn/(double)
							// animalVocalisations.getVocTimes().length*recievers.getArrayXYZ().length);
						}


						// figure out what the received level info should be from the sensors.
						recInfo = getRecievedInfoSnap(vocTimesD, recievedLevels, distances, heights, j);
			
						
					}
					
//					if (recInfo==null) continue;

					count++; 
//					if (recInfo==null) {
//						System.out.println("vocTimesD: " + vocTimes.size());
//					}
					
					if (recInfo.distance > simSettings.maxRange) {
				
						// this is very important - in distance sampling we consider a specified area -
						// if the distance is
						// greater than the maximum range then we are outside the area and the result
						// should not be counted
						// at all.
						//System.out.println("recInfo.distance: " + recInfo.distance);
					} 
					else {
						// add the results to an array
						trackRecInfo.add(recInfo);
					}
					
					
				}
				
				
				for (int i=0; i<trackRecInfo.size() ;i++) {
					if (trackRecInfo.get(i).recievedLevel>(simSettings.noise + simSettings.snrThreshold)) {
						sum+=1; 
					}
				}
				

				// now must add the results to the histrogram
				pDet.addToHist(trackRecInfo, 1.0, simSettings.noise + simSettings.snrThreshold);
				effortDet.addToHist(trackRecInfo, null, Double.NEGATIVE_INFINITY); // will always be greater
				
//				if (trackRecInfo.size()>400) {
//					System.out.println("Add track record info: " + trackRecInfo.size() + " timebin n: " + k + " of " +  
//					timeBins.length + " effort count: " + pDet.getTotalcount() + " total det. " + sum ); 
//					for (int bb=0; bb<trackRecInfo.size(); bb++) {
//						System.out.print(String.format("%.2f", trackRecInfo.get(bb).distance));
//					}
//				}
				

				if (k % 10 == 0) {
					notifyStatusListeners(StatusListener.SIM_RUNNING, n, k / (double) timeBins.length);
				}

				// now have a giant array of distances and received levels.
			}
						
			probDetResults.add(pDet);
			probDetEffort.add(effortDet);
		}

		notifyStatusListeners(StatusListener.SIM_FINIHSED, animal.getNumberOfAnimals().intValue(), 1);

	}

	/**
	 * Get the received info for a single snapshot. This will dictate whether a
	 * snapshot is detected or not. Here the highest received level is used to
	 * create the snapshot, however, this function could be used for more complex
	 * detection algorithms. For example, if a click train detector were used then
	 * this function could be overridden to consider whether a <i>click train</i>
	 * was detected or not. Settings the RecievedInfo received level > threshold
	 * would indicate a detection and < threshold would indicate no detection.
	 * 
	 * @param times          - the times of each detection in seconds from start
	 * @param recievedLevels - the received levels of each detection in dB re 1uPa -
	 *                       -1 indicates that the detection was out of range.
	 * @param distances      - the distances to each detection.
	 * @param heights        - the depth of the animal for each detection.
	 * @param recieverID     - the ID of the receiver.
	 * @return the RecievdInfo which is used to indicate whether a detection was
	 *         made or not.
	 */
	public RecievedInfo getRecievedInfoSnap(double[] times, double[] recievedLevels, double[] distances,
			double[] heights, int recieverID) {
		
		
		double recievedLevel = Double.NEGATIVE_INFINITY;
		int index = -1;

		// choose the maximum recieve3d level - i.e. a snapshot will be positive if at
		// least one click is greater
		// than threshold in the subsequent pdet calculation.
		for (int i = 0; i < times.length; i++) {
			if (recievedLevels[i] > recievedLevel) {
				recievedLevel = (float) recievedLevels[i];
				index = i;
			}
		}
		
		try {
		return new RecievedInfo((float) recievedLevel, (float) distances[index], (float) heights[index], recieverID);
		}
		catch (Exception e) {
			System.out.println("ERROR in index: " + recievedLevels.length +  " " + times.length);
			for (int i=0; i<recievedLevels.length; i++) {
				System.out.println("RecievedLevel " + i + " : " + recievedLevels[i] );
			}
			return null;
		}
	}

	/**
	 * Get time bins between a start and end time for a specified bin length.
	 * 
	 * @param timestart - the start time in seconds.
	 * @param timeend   - the end time in seconds.
	 * @param bin       - the bin time in seconds.
	 * @return a list of time bins - {{bin start, bin end},...};
	 */
	private double[][] getTimeBins(double timestart, double timeend, double bin) {
		double timebin = timestart;

		ArrayList<double[]> timeBins = new ArrayList<double[]>();
		while (timebin < timeend) {
			timeBins.add(new double[] { timebin, timebin + bin });
			timebin = timebin + bin;
			//System.out.println("timebin: " +timebin +  " timestart: " + timestart + " timeend " + timeend);
		}
		
		
		double[][] timeBinsD = new double[timeBins.size()][];
		for (int i=0; i<timeBinsD.length; i++) {
			timeBinsD[i] = timeBins.get(i); 
		}
		
		return timeBinsD; 
	}

	/**
	 * Get the histograms for each animal which are the number of detected clicks in
	 * each range bin.
	 * 
	 * @return the probability of detection results in different range and depth
	 *         bins.
	 */
	public ArrayList<Hist3> getProbDetResults() {
		return probDetResults;
	}

	/**
	 * Get the histograms for each animal's track effort i.e. the number of clicks
	 * which occurred in each range and depth bin over all devices
	 * 
	 * @return the track effort results in different range and depth bins.
	 */
	public ArrayList<Hist3> getTrackEffortResults() {
		return probDetEffort;
	}

	/**
	 * Notify the status listeners of a change in state or progress of the
	 * simulation
	 * 
	 * @param actionFlag        - the action flag. Shows state of simulation
	 * @param progressBootstrap - the number of bootstraps completed.
	 * @param progressSim       - the progress of the simualtion.
	 */
	private void notifyStatusListeners(int actionFlag, int progressBootstrap, double progressSim) {
		for (int i = 0; i < statusListeners.size(); i++) {
			statusListeners.get(i).statusAction(actionFlag, progressBootstrap, progressSim);
		}
	}

	/**
	 * Clear all status listeners from the simulation.
	 */
	public void clearStatusListeners() {
		statusListeners.clear();
	}

	/**
	 * Remove a status listener.
	 * 
	 * @param status true if the status listener was removed. False if it wasn't in
	 *               the list.
	 */
	public boolean removeStatusListener(StatusListener status) {
		return this.statusListeners.remove(status);
	}

	/**
	 * Add a status listener to the track simulation. This returns status flags and
	 * progress information.
	 * 
	 * @param status - the status listener to add
	 */
	public void addStatusListener(StatusListener status) {
		this.statusListeners.add(status);

	}

	/**
	 * Enable the printing of status. This will print the progress regularly to the
	 * console.
	 */
	public void enableStatusPrint() {
		enableStatusPrint(null);
	}

	/**
	 * Enable the printing of status. This will print the progress regularly to the
	 * console.
	 * 
	 * @param extraString - this is an extra string that will be printed at the end
	 *                    of every status update.
	 */
	public void enableStatusPrint(String extraString) {
		final long millis = System.currentTimeMillis();
		// add a status listener to print info about the current progress of the
		// simulation.

		final String endStr = extraString == null ? "" : extraString;

		addStatusListener((actionFlag, progressBootstrap, progressSim) -> {
			System.out
					.println(String.format("Progress sim: %.3f", 100 * progressSim) + "%"
							+ String.format(" Expected total time: %.2f hours",
									((System.currentTimeMillis() - millis) / 1000.) / progressSim / 60 / 60)
							+ " " + endStr);
		});
	}

	/**
	 * A class with fields actually has a smaller memory footprint than a double[]
	 * array. Note: use floats to reduce size in memory
	 * 
	 * @author Jamie Macaulay
	 *
	 */
	public class RecievedInfo {

		/**
		 * The received level in dB re 1uPa pp
		 */
		public float recievedLevel;

		/**
		 * The <b>2D</b> distance in meters.
		 */
		public float distance;

		/**
		 * The depth of the animal. Under water is negative. So ten meters under water
		 * -> 10.
		 */
		public float height;

		/**
		 * The receiver ID i.e. the index of the receiver in the receiver list.
		 */
		public int recieverID;

		public RecievedInfo(float recievedLevel, float distance, float height, int recieverID) {
			this.recievedLevel = recievedLevel;
			this.distance = distance;
			this.recieverID = recieverID;
			this.height = height;
		}
	}

}
