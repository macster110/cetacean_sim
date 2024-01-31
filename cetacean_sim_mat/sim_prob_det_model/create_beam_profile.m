function [ Xq,Yq,Vq, beamraw ] = create_beam_profile(type, varargin)
%CREATEPORPBEAMPROFILE Creates an interpolated surface of a beam profile.
% [ XQ,YQ,VQ, BEAMRAW ] = CREATE_BEAM_PROFILE(TYPE, VARARGIN) creates a
% beam profile for an animal of a specific TYPE with optional additional
% arguments VARAGRIN. Animals can be;
% * 'porp' - Koblitz 2012 beam profile with bottlenose emasured side (DEPRACATED).
% * 'porp_new' -Koblitz et al. 2012 measure beam profile with prop
% piston model for angles >15 degrees.
% * 'porp_piston' - beam profile based entirely on a piston model. Input
% argument 'aperture' APERTURE can be used to specify the aperture
% _radius_ in cm. Argument 'vertcompression' VERTCOMPRESSION compresses
% the beam vertically by a VERTCOMPRESSION degrees.
% * 'porp_buzz' Porpoise buzz wider beam profile based on Wisniewska et al. 2015
% * 'porpbackend' - is 'porp' with an added small back end beam profile. 
% * 'porpnoside' - is 'porp' without any side energy (i.e. only front facing)
% * 'bat' - bat beam profiule (cannot remember where from)
% * 'uniform' - uniform beam profile. 
% * 'daubentonii' - M. daubentonii (Daubetons bat) beam prfile from (Jakobsen, Ratcliffe and Surlykke, 2013)


nargin=1; 
% type='porp';

aperture = 0.075/2; % aperture diameter 

pistonmodel=[]; 

ndTL=-40; %lowest piston model predicts

iArg = 0;
while iArg < numel(varargin)
    iArg = iArg + 1;
   switch(varargin{iArg})
       case 'aperture'
           iArg = iArg + 1;
           aperture = varargin{iArg};
       case 'vertcompression'
           iArg = iArg +1; 
           vertcompression = varargin{iArg}; %degrees
       case 'pistonmodel'
           iArg = iArg +1; 
           pistonmodel = varargin{iArg}; %piston model
       case 'nodataTL'
           % the tranmission loss ofr portions of the beam without data
           iArg = iArg +1; 
           ndTL = varargin{iArg}; %piston model
   end          
end

if (nargin~=0 && ~ischar(type))
    
horz_degrees= type(:,1);
vert_degrees=type(:,2);
TL=type(:,3);

elseif (nargin==0 || strcmp(type,'porp'))

horz_degrees=  [-180,180,-180,180,0,0,-180,-90,-15,-10,10,22,90 180,0.0,0.0,0.0,0.0,0.0];
vert_degrees=[-90,90,90,-90,-90,90,0,0,0,0,0,0,0,0,5,10,-10,-3,0];
TL=[ndTL,ndTL,ndTL,ndTL,-35,-35,ndTL,-35,-13.2,-7.5,-6, -20, -35, ndTL,-3,-12,-9, -1.5,0];

elseif (nargin==0 || strcmp(type,'porp_new'))
% the porpoise with piston model. 
    
horz_degrees=  [-15,-10,10,22,90 180,0.0,0.0,0.0,0.0,0.0];
vert_degrees=[0,0,0,0,0,0,5,10,-10,-3,0];
TL=[-13.2,-7.5,-6, -20, -35, -50,-3,-12,-9, -1.5,0];

    piston  = load('tc2130_piston_impulse.mat');
    piston= piston.tcpiston; 
    
    % use Jens measuremtns for front of beam 
    piston=piston(piston(:,1)>15,:); 
    
    % add now
    horz1 = piston(:,1); 
    horz2 = -piston(:,1); 
    horz3 = zeros(length(piston(:,1)),1); 
    horz4 = zeros(length(piston(:,1)),1); 
    
    vert1 =  zeros(length(piston(:,1)),1); 
    vert2 =  zeros(length(piston(:,1)),1); 
    vert3 =  piston(:,1)-2; % ventrally compress a little??
    vert4 = -(piston(:,1)-2);    
    
    % horizonatal degrees
    horz_degrees=[horz_degrees, horz1', horz2', horz3', horz4'];
    vert_degrees=[vert_degrees vert1', vert2', vert3', vert4'];
    TL = [TL piston(:,2)' piston(:,2)' piston(:,2)' piston(:,2)']; 
  
elseif (nargin==0 || strcmp(type,'porpbuzz'))
% Wisniewska et al 2015. These are the data from the larger array which
% measured beam width. -3dB beam increased on averge from 9.1 to 15.1
% degrees. 

wb=15.1/9.1; % how much to widen the beam by

[ ~,~,~, beamraw ] = measured_porp_beam(); 

for i=1:length(beamraw)
    if (abs(beamraw(i,1)<30) || abs(beamraw(i,2)<30))
       %widen 
       beamraw(i,1)=beamraw(i,1)*wb;
       beamraw(i,2)=beamraw(i,2)*wb;
    end
end

horz_degrees = beamraw(:,1)';
vert_degrees = beamraw(:,2)';
TL = beamraw(:,3)';


elseif (nargin==0 || strcmp(type,'porpbuzz_old'))
    
wb=15.1/9.1; % how much to widen the beam by
horz_degrees=  [-180,180,-180,180,0,0,-180,-90,-15*wb,-10*wb,10*wb,22*wb,90 180,0.0,0.0,0.0,0.0,0.0];
vert_degrees=[-90,90,90,-90,-90,90,0,0,0,0,0,0,0,0,5*wb,10*wb,-10*wb,-3*wb,0];
TL=[-50,-50,-50,-50,-35,-35,-50,-35,-13.2,-7.5,-6, -20, -35, -50,-3,-12,-9, -1.5,0];

elseif strcmp(type,'porpnoside')
    
ndTL=-80;
%porpoise beam with no side beyone -30 and 30 degrees. 
    
% horz_degrees=  [-180,180,-180,180,0,0,-180,-90,-15,-10,10,22,90 180,0.0,0.0,0.0,0.0,0.0];
% vert_degrees=[-90,90,90,-90,-90,90,0,0,0,0,0,0,0,0,5,10,-10,-3,0];
% TL=[ndTL,ndTL,ndTL,ndTL,ndTL,ndTL,ndTL,ndTL,-13.2,-7.5,-6, -20, ndTL, ndTL,-3,-12,-9, -1.5,0];

[horz_degrees, vert_degrees, TL] = createpistonbeam(aperture, 0, ndTL, [-30,30]);

elseif strcmp(type,'porp_piston')
    
[horz_degrees, vert_degrees, TL] = createpistonbeam(aperture, 0, ndTL);

elseif strcmp(type,'porp_measured')
    [ Xq,Yq,Vq, beamraw ] = measured_porp_beam(); 
    return; 
    
elseif strcmp(type,'bat')
    
horz_degrees =      [-180,180,-180,180,       0,0,-180, -90,          -60,   -30,  30,  60,        90   180,          0, 0, 0, 0,            0];
vert_degrees = [-90, 90,90,-90,          -90, 90, 0,0,             0,     0,    0,   0,        0,   0,            -60,  -30,  30, 60,   0];
%TL =           [-50, -50, -50, -50, -35, -35, -50,  -35,     -25, -20, -20,  -30, -35, -50,       -10,  -20, -20,  -3, 0];
%TL =           [-50, -50, -50, -50, -35, -35, -50,  -35, -13.2, -7.5, -6,  -20, -35, -50, -3,  -12, -9,  -1.5, 0];
%TL =           [0, 0, 0, 0, 0, 0, 0,  0, 0, 0,0,0,0,0,0,0,0,0, 0];
%TL =           [0, 0, 0, 0, 0, 0, 0,  0, 0, 0,0,0,0,0,0,0,0,0, 0];
%TL =           [-40, -40, -40, -40,     -25, -25, -40,  -25,       -18, -6,  -6, -18,         -25, -40,          -18, -6,  -6, -18,         0]; %OPEN
%TL =           [-39, -39, -39, -39,     -24, -24, -39,  -24,       -17, -5,  -5, -17,         -24, -39,          -17, -5,  -5, -17,         0]; %EDGE
TL =           [-40, -40, -40, -40,     -25, -25, -40,  -25,       -20, -10,  -10, -20,         -25, -40,          -20, -10,  -10, -20,         0]; %NARROW


elseif strcmp(type,'daubentonii')
    
horz_degrees = [-180,180,-180,180,       0,   0,-180,       -80, 80,  -30,  30, -60,  60         180,           0, 0, 0, 0,            0];
vert_degrees = [-90, 90,90, -90,         -60, 60, 0,         0,   0,    0,  0,   0,   0,           0,          30,  -30,  15, -15,      0];
TL =           [-40, -40, -40, -40,     -12, -12, -40,       -21, -21, -5,  -5,  -15, -15,        -40,          -5, -5,  -2, -2,         0]; %NARROW


elseif strcmp(type,'uniform')
    
horz_degrees=        [-180,180,-180,180,0,0,-180,-90,-15,-10,10,22,90 180,0.0,0.0,0.0,0.0,0.0];
vert_degrees=   [-90,90,90,-90,-90,90,0,0,0,0,0,0,0,0,5,10,-10,-3,0];
TL=             [0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0];

end

beamraw=[horz_degrees; vert_degrees; TL ]'; 

% sampleValues=off_axis_angle;
xv = linspace(min(horz_degrees), max(horz_degrees),200);
yv = linspace(min(vert_degrees), max(vert_degrees), 100);
[Xinterp,Yinterp] = meshgrid(xv,yv); 

[Xq,Yq,Vq]  = griddata(horz_degrees,vert_degrees,TL,Xinterp,Yinterp,'cubic');

%THIS DOES NOT WORK -DO NOT USE - REMOVE IF THE NUMBER VALUES IN THE ROW
%ARE THE SAME
% [~, ia] = unique(beamraw([1 2],:), 'rows', 'stable');
%  % get rid of any duplicate points that mess up surface interpolators.
% beamraw=beamraw(ia,:);


end

function [horz_degrees, vert_degrees, TL] = createpistonbeam(aperture, sigtype, ndTL, minmaxangs)
%CREATEPISTONBEAM creates a set of angles for a 3D piston beamprofile surface

% a piston model of the harbour porpoise. 
% work out total angle. 

vertcompression = 2; %degree 

if nargin<2
    sigtype = 0; %harbour porpoise
end

if nargin<3
    ndTL = -40; %dB rest of beam
end

if nargin<4
    minmaxangs =[-89.9, 89.9]; %full piston beam
end

back_attenuation =ndTL; % the back beam attenuation in dB 

minang = minmaxangs(1)+0.1;
maxang = minmaxangs(2)+0.1; 
n=1;
for horz=minang:5:maxang
    for vert=minang:5:maxang
        
        x	=	cosd(horz)*sind(90-vert);
        y	=	sind(horz)*sind(90-vert);
        z	=	cosd(90-vert);
        u=[x,y,z];
        v=[1,0, 0]; 
        
        %make the vertical compensation factor. 
        vertcomp=(vert/90)*vertcompression; 
        
        total_angle(n) = atan2d(norm(cross(u,v)),dot(u,v))-vertcomp;
        
        horz_degrees(n)=horz;
        vert_degrees(n)=vert;        
        n=n+1;
    end
end

%run the piston model with angles

[TL] = pistonmodel_kristian(total_angle, 'aperture', aperture, 'sigtype', sigtype); 

% add the back end beam @ -40dB - must use lots of angles or interpolation
% leads to larger beam profile than is intended. 

backangshorz =[-180:3:(minang-1) (maxang+1):3:180]; 
% backangvert = [-90:minang maxang:90];
backangvert = [-89.9:3:89.9];
n=1;
for horzang=backangshorz
    for vertang = backangvert
    horz_degrees_bck(n) = horzang;
    vert_degrees_bck(n) = vertang;
    n=n+1; 
    end
end

% horz_degrees_bck=  [-180,180,-180,180,-180];
% vert_degrees_bck=[-90,90,90,-90,0];
TLbck = back_attenuation*ones(length(vert_degrees_bck),1); 

%combine piston model and back end together. 
horz_degrees=[horz_degrees horz_degrees_bck];
vert_degrees=[vert_degrees vert_degrees_bck];

TL=[TL(1,:) TLbck'];

end
