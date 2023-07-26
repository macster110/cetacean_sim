function [probdet, effortdet, hydrophonearray, animalStruct] = simprobdettrackj(tagdata2, settings, varargin)
%SIMPROBDETTRACKJ Run the probability of detection simulation for a track
%using java.
%   PROBDET =  SIMPROBDETTRACKJ(TRACKSTRUCT, SETTINGS) runs a probability of
%   detection simualtion by converting TRACKSTRUCT into a Java format and
%   then running the simualtion on CetSim - a java program - with specified
%   SETTINGS. This greatly increases speed compared to using MATLAB native
%   code (which is based on Java anyway)
%
%   Note that for this function to work, you need to increase the Java heap
%   size in MATLAB's preferences.

% load the track data.
% load('/Users/au671271/Desktop/tagDataTest.mat');

cornerloss =  -45; %dB

% convenience to make variables a little shorter.
maxrange=           settings.maxrange; %meters
rangebins=          settings.rangebins;
depthbins=          settings.depthbins;
maxdepth=           settings.maxdepth; %meters
gridSpacing =       settings.gridSpacing; %meters
snrThresh =         settings.snrThresh;
spreadingCoeff =    settings.spreadingCoeff;
absorptionCoeff =   settings.absorptionCoeff;
noise =             settings.noise;
snap =              settings.snap;%true to use snap approach to pdet
snaptimebin =       settings.snaptimebin; %seconds - the time bin if using snap
% iArg = 0;
% while iArg < numel(varargin)
%     iArg = iArg + 1;
%     switch(varargin{iArg})
%         case 'Prefix'
%             iArg = iArg + 1;
%             prefix = varargin{iArg};
%     end
% end
prefix='';

iArg = 0;
while iArg < numel(varargin)
    iArg = iArg + 1;
    switch(varargin{iArg})
        case 'Prefix'
            iArg = iArg + 1;
            prefix = varargin{iArg};
        otherwise
            iArg = iArg + 1;
    end
end


% need to add the jar to the java path
p = mfilename('fullpath');
[filepath,~,~] = fileparts(p);

% javajarpath=[filepath '/cetsim_1_0.jar'];
%new version with snap prob det
javajarpath=[filepath '/cetsim_1_1.jar'];

disp(javajarpath);

javaaddpath(javajarpath);

%% convert the track data to an animal struct.
%get rid of any NaN depth values...
% index = find(isnan(tagdata2.trackdata(:,4)));
% tagdata2.trackdata(index,4) = 0;

animalStruct  = simulation.probdettrack.AnimalStruct;
animalStruct.clicks = tagdata2.clicks;
animalStruct.trackdata = tagdata2.trackdata;



animalStruct.orientation = tagdata2.orientation; %already un RADIANS
animalStruct.vp2p = java.lang.Double(tagdata2.vp2p);
animalStruct.systemSens = java.lang.Double(tagdata2.sens);
% the beam profile might be included in the tag data or could be externel.
if (isfield(tagdata2, 'beamprofile'))
    beamraw = tagdata2.beamprofile;
else
    [Xq, Yq, tlq, beamraw] = measured_porp_beam();
end

beamraw(:,[1 2]) = deg2rad(beamraw(:,[1 2]));

animalStruct.beamProfile = beamraw;

%% now run the simualtion
trackImport = simulation.probdettrack.TrackMATImport;

%convert the raw data in the struct into a Java animal model object.
animalModel = trackImport.animalStruct2AnimalModel(animalStruct);

%create the hydrophone array
hydrophoneArray = reciever.GridHydrophoneArray(animalModel.getTrack(0).xyz, gridSpacing, settings.griddepth, maxrange);

hydrophonearray = hydrophoneArray.getArrayXYZ();
for i=1:length(hydrophonearray)
    fprintf('Reciever %d x: %.2f y: %.2f, z: %.2f\n', i, ...
        hydrophonearray(i,1), hydrophonearray(i,2),  hydrophonearray(i,3));
end

%create the track simulation class.
probDetTrack = simulation.probdettrack.ProbDetTrack;

% create the settings obejct for the simulation
probDetTrackSettings = simulation.probdettrack.ProbDetTrackSettings(animalModel,  hydrophoneArray, ...
    noise,  snrThresh,  spreadingCoeff, absorptionCoeff);
probDetTrackSettings.minHeight=-maxdepth;
probDetTrackSettings.numDepthBins=depthbins;
probDetTrackSettings.numRangeBins=rangebins;
probDetTrackSettings.maxRange=maxrange;
%probDetTrackSettings.useRoll=false; %%% DELETE %%%
probDetTrackSettings.snapTime = snaptimebin;
disp(['Roll enabled ' num2str(probDetTrackSettings.useRoll)])

%set the time limits for the snapshot method. 
timelims = tagdata2.tagonoff_sec; 

%VERY IMPORTANT the java simualtion is referenced in time from sim start
%- if the simualtion start is not defined when loading the animal struct
%then the the first track point is used instead. Must use this to reference
%the simualtion time bins. 
probDetTrackSettings.snapTimeLims = timelims - (tagdata2.trackdata(1,1)-tagdata2.tagstart)*60*60*24; 

%print the current progress of the simulation
probDetTrack.enableStatusPrint(prefix);

%setup the simulation
probDetTrack.setupProbTrack(probDetTrackSettings);

%run the simulation
if (snap)
    probDetTrack.runProbTrackSnap(probDetTrackSettings);
else
    probDetTrack.runProbTrack(probDetTrackSettings);
end

%get the histogram results
probDetResults = probDetTrack.getProbDetResults();
trackEffortResults = probDetTrack.getTrackEffortResults();

%now need to get a histogram surface from the historgram surface.
for i=0:probDetResults.size()-1
    probdet(i+1).hist = probDetResults.get(i).getHistogram();
    probdet(i+1).ybinedges = probDetResults.get(i).getYbinEdges();
    probdet(i+1).xbinedges = probDetResults.get(i).getXbinEdges();
    probdet(i+1).totalcount = probDetResults.get(i).getTotalcount();
end


%now need to get a histogram surface from the historgram surface.
for i=0:trackEffortResults.size()-1
    effortdet(i+1).hist = trackEffortResults.get(i).getHistogram();
    effortdet(i+1).ybinedges = trackEffortResults.get(i).getYbinEdges();
    effortdet(i+1).xbinedges = trackEffortResults.get(i).getXbinEdges();
    effortdet(i+1).totalcount = trackEffortResults.get(i).getTotalcount();
end

javarmpath(javajarpath)

end

% function object = struct2class(classname, s)
%    %converts structure s to an object of class classname.
%    %assumes classname has a constructor which takes no arguments
%    object = eval(classname);  %create object
%    for fn = fieldnames(s)'    %enumerat fields
%       try
%           object.(fn{1}) = s.(fn{1});   %and copy
%       catch
%           warning('Could not copy field %s', fn{1});
%       end
%    end
% end

% end