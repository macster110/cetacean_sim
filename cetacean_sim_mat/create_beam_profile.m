function [ Xq,Yq,Vq, beamraw ] = create_beam_profile(type)
%CREATEPORPBEAMPROFILE Create an interpolated surface of a beam profile. 

nargin=1; 
% type='porp';

if (nargin~=0 && ~ischar(type))
    
horz_degrees= type(:,1);
vert_degrees=type(:,2);
TL=type(:,3);

elseif (nargin==0 || strcmp(type,'porp'))

horz_degrees=  [-180,180,-180,180,0,0,-180,-90,-15,-10,10,22,90 180,0.0,0.0,0.0,0.0,0.0];
vert_degrees=[-90,90,90,-90,-90,90,0,0,0,0,0,0,0,0,5,10,-10,-3,0];
TL=[-50,-50,-50,-50,-35,-35,-50,-35,-13.2,-7.5,-6, -20, -35, -50,-3,-12,-9, -1.5,0];

elseif (nargin==0 || strcmp(type,'porpbuzz'))

wb=19.3/13.0; 
horz_degrees=  [-180,180,-180,180,0,0,-180,-90,-15*wb,-10*wb,10*wb,22*wb,90 180,0.0,0.0,0.0,0.0,0.0];
vert_degrees=[-90,90,90,-90,-90,90,0,0,0,0,0,0,0,0,5*wb,10*wb,-10*wb,-3*wb,0];
TL=[-50,-50,-50,-50,-35,-35,-50,-35,-13.2,-7.5,-6, -20, -35, -50,-3,-12,-9, -1.5,0];

elseif (strcmp(type,'porpbackend'))
    
horz_degrees=        [-180,180,-180,180,0,0,-180,-90,-15,-10,10,22,90 180,0.0,0.0,0.0,0.0,0.0];
vert_degrees=   [-90,90,90,-90,-90,90,0,0,0,0,0,0,0,0,5,10,-10,-3,0];
TL=             [-50,-50,-50,-50,-35,-35,-21,-35,-13.2,-7.5,-6, -20, -35, -21,-3,-12,-9, -1.5,0];

elseif strcmp(type,'porpnoside')
    
horz_degrees=  [-180,180,-180,180,0,0,-180,-90,-15,-10,10,22,90 180,0.0,0.0,0.0,0.0,0.0];
vert_degrees=[-90,90,90,-90,-90,90,0,0,0,0,0,0,0,0,5,10,-10,-3,0];
TL=[-200,-200,-200,-200,-200,-200,-200,-200,-13.2,-7.5,-6, -20, -200, -200,-3,-12,-9, -1.5,0];

elseif strcmp(type,'porpsymm')
    
[ newwave ] = simclick_piston( 0, 500000, 0); 
wavamp=20*log10(max(newwave)-min(newwave)/0.000001); 

n=1;
for horz=-180:5:180
    for vert=-90:5:90
        
        x	=	cosd(horz)*sind(90-vert);
        y	=	sind(horz)*sind(90-vert);
        z	=	cosd(90-vert);
        u=[x,y,z];
        v=[1,0, 0]; 
        
        total_angle(n) = atan2d(norm(cross(u,v)),dot(u,v));
        
        %no dot product with the axis
        if (abs(total_angle(n))>35)
            tl=-total_angle(n);
        else
            [ newwave ] = simclick_piston( 0, 500000,  total_angle(n));
            tl = 20*log10(max(newwave)-min(newwave)/0.000001)-wavamp;
        end
        
        horz_degrees(n)=horz;
        vert_degrees(n)=vert;
        TL(n)=tl;
        
        n=n+1;
    end
end


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

end

