function [ probmeanq,  probstdq, angrecieve] = sim_prob_det_java(settings, angles, prefix)
%SIM_PROB_DET_JAVA Simulates the probability of detection using a java
%library 
%   [PROBMEANQ, PROBSTQ] = SIM_PROB_DET_MONTE3D(SETTINGS)
%   calculates the probability of detection based on parameter distributions
%   in SETTINGS such as beam profile, source levels, vertical angle and
%   horixontal angle. PROBMEANINTERP is a structure contiang the surface
%   and PROBSTDINTERP is a structure containing the standard deviation in
%   the surface. 
%
%   [PROBMEANQ, PROBSTQ, ANGRECIEVE] = SIM_PROB_DET_MONTE3D(SETTINGS, ANGLES) 
%   records all angles of animals at depths which were successfully
%   detected in the simulation if ANGLES is true. Note that the angles are 
%   only recorded for the last simulation run, not all bootstraps.

%   [PROBMEANQ, PROBSTQ, ANGRECIEVE] = SIM_PROB_DET_MONTE3D(SETTINGS, ANGLES, PREFIX) 
%   adds a string PREFIX to the progress string output. 
%
%   Note: If this function does not work (e.g. by saying it cannot find classes)
%   it may be that MATLAB is not running the same verson of Java as cetsim.

if nargin<2
   angles=false;  
end
 angrecieve=[]; 
 

if nargin<3
   prefix='';   
end

% if nargin==0  
%     %%TEST%%%%%
%     % same as default settings in cetsim.
%     settings= clickmethod_settings_jm('porp');
%     settings.n=10;
%     settings.N=50000;
%     settings.maxdepth=-200;
%     settings.maxRange=750;
%     settings.thresh=100;
%     settings.rangebin=25;
%     %%%%%%%%%%%
% end

% need to add the jar to the java path 
p = mfilename('fullpath'); 
[filepath,~,~] = fileparts(p); 

javajarpath=[filepath '\cetsim.jar']; 

disp(javajarpath); 

javaaddpath(javajarpath);

% javaaddpath('E:\Google Drive\SMRU\Programming\MATLAB\sim_prob_detection\MCDetectionSimulation_java\cetsim.jar')

%% create the animal object
% animal2j=animal.SimpleOdontocete(settings.srclevel, settings.srcstd, ...
%     deg2rad(settings.vertangle) ,deg2rad(settings.vertstd), settings.vertdepthlim,...
%     settings.maxdepth, settings.mindepth, 'porp');
sourcelevelsim= matlab2javasimvar('Source Level', settings.sourcelevel); 
horzangle=      matlab2javasimvar('Horizontal Angle', prob_dist2rad(settings.horzangle)); %cetsim uses rad
depthdist=      matlab2javasimvar('Depth', settings.depthdist); 

for i=1:length(settings.vertangle)
    vertangles(i)=matlab2javasimvar('Vert Angle', prob_dist2rad(settings.vertangle(i)));%cetsim uses rad
end

%convert beam measurements to rad
beamraw=settings.beamraw;
beamraw(:,1)=deg2rad(beamraw(:,1));
beamraw(:,2)=deg2rad(beamraw(:,2));

%the animal
animal2j=animal.SimpleOdontocete(sourcelevelsim, vertangles, horzangle, ...
   depthdist, beamraw, settings.maxdepth); 

%% set the correct hydrophones 
hydrophones=settings.hydrophones;


%% create the settings object for the simulation
settingsj=simulation.probdetsim.ProbDetSimSettings(animal2j, hydrophones, ...
    settings.N, settings.n, settings.maxrange, settings.maxdepth, ...
    settings.rangebin, settings.depthbin, settings.noise, settings.snr_thresh,...
    settings.spreading_coeff, settings.abs_coeff); 

%oops.forgot to add this to constructor
settingsj.minRecievers=settings.minhydrophones; 
settingsj.evenXY=settings.evenxy; 

%the detector efficiecny
detectorj = detector.SimpleDetector(settings.det_efficiency.customp, ...
    settings.det_efficiency.min,  settings.det_efficiency.max,...
     settings.perfect_detector);

settingsj.detector = detectorj; 

% print out the settings. 
settingsj.printSettings;

%% read the Monte Carlo simualtion object
probDetMonteCarloj = simulation.probdetsim.ProbDetMonteCarlo;

%set a prefix. 
probDetMonteCarloj.setPrefix(prefix); 

%get the simualtion record angles if true. 
if (angles)
    probDetMonteCarloj.setRecordAngles(true);  
end

%run the simualtion 
probDetMonteCarloj.run(settingsj, true);
probDetResultj=probDetMonteCarloj.getResult; 

%get the results from the java object
pmean=probDetResultj.probSurfaceMean.getHistogram; 
pstd=probDetResultj.probSurfaceStd.getHistogram; 

xbinedges=probDetResultj.probSurfaceMean.getXbinEdges;
ybinedges=probDetResultj.probSurfaceMean.getYbinEdges;

%interpolate the surface 
[probmeanq] =  interpprobsurf(pmean, xbinedges, ybinedges);
[probstdq] =   interpprobsurf(pstd, xbinedges, ybinedges);

if (angles)
    angrecieve= probDetMonteCarloj.getAngles; 
end


    function [simVar] = matlab2javasimvar(name, probdistval)
        % the switch value
        switch lower(probdistval.type)
            case 'normal'
                simVar=simulation.NormalSimVariable(name, probdistval.mean, probdistval.std); 
                simVar.setLimits(probdistval.limits); 
            case 'log-normal'
                simVar=simulation.LogNormalSimVar(name, probdistval.scale,...
                    probdistval.shape, probdistval.truncation, probdistval.flipnegative); 
                simVar.setLimits(probdistval.limits); 
            case 'uniform'
                simVar=simulation.RandomSimVariable(name, probdistval.min, probdistval.max); 
                simVar.setLimits(probdistval.limits); 
            case 'custom'
%                 simVar=simulation.CustomSimVar(); 
% disp([ probdistval.customp])
                simVar=simulation.CustomSimVar(name, probdistval.customp, probdistval.min, probdistval.max); 
                simVar.setLimits(probdistval.limits); 
            otherwise
                disp(['Error: SIM_PROB_DET_JAVA - could not convert sim variable: ' probdistval.type]); 
        end
    end

end

