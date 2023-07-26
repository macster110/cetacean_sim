function [p] = clickmethod_settings_jm(species)

% Provide some info about this simulation
if nargin==0
    species = 'porp'; % Species ID (for output file name)
end

%% basic simulation settings
% Number of model iterations
p.n = 50;
% Clicks to simulate per model
p.N = 50000;

%if 0 then the points in the simulation are spaced evenly in x and y and
%thus range is triangular distriubtion. If 1 then the ranges are evenly
%distributed as so in x and y the 
p.evenxy=0; 

p.maxrange = 750; % Detection range cutoff in meters#
p.maxdepth = -180; % The maximum depth for the simulation to consider.
p.mindepth = 0; % The minimum depth for the simualtion to consider


p.depthbin=10;% the number of depth bins
p.rangebin=50;%the number of range bins.

%% noise
p.noise = 100; % Minimum RL cutoff for detectability, in dB_pp re 1uPa,
p.snr_thresh = 16; % Minimum RL cutoff for detectability, in dB

%% hydrophone array 
p.minhydrophones=4; %the minimum number of hydrophones.

%hydrophones are in 3D x y z. 
[ ~, ~, p.hydrophones ] = get_array_Sil_14( 0 ); 

%% transmission loss
p.spreading_coeff=20;
p.abs_coeff=0.04;

%% detector efficiency
dafaultdetefficiency =[0.3, 0.4, 0.49, 0.58, 0.66, 0.73, 0.79, 0.85, 0.89, 0.91, 0.92,... 
		0.93, 0.93, 0.945, 0.95, 0.96, 0.965, 0.97, 0.975, 0.98, 0.985, 0.99, 1.0];
p.det_efficiency=prob_dist('custom', {0, 100, dafaultdetefficiency}); 
p.perfect_detector=false; 


%% the vertical angle distribution
p.vertangle=[prob_dist('normal', [0, 19], [-10,0]); ...
    prob_dist('normal', [0, 27], [-10000, -10]); ];

%% source level distribution
p.sourcelevel=prob_dist('normal', [170, 20]); 

%% the horizontal angle distribution
p.horzangle=prob_dist('uniform', [-180, 180]); 

%% the depth distribution
p.depthdist=prob_dist('uniform', [p.maxdepth, 0]); 

%% beam profile.
[ Xq,Yq,Vq, beamraw] = create_beam_profile(species);
p.horzBeam=Xq;
p.vertBeam=Yq;
p.tLBeam=Vq;

p.beamraw=beamraw;
%% legacy settings

%type of contour measurment
p.contourtype='eff50';
p.contperc= 0.95; %only used if contourtype = "nperc";
p.hydrophonedepth=p.hydrophones(:,3); 

end
