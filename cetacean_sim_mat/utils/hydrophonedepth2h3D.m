function [hydrophones3d] = hydrophonedepth2h3D(hydrophonedepths)
%HYDROPHONEDEPTH2H3D Converts hydrophone depths to 3D co-oridntaes assuming
%all hydrophones located at (x,y)=(0,0); 
%   Detailed explanation goes here

hydrophones3d=zeros(length(hydrophonedepths), 3); 

hydrophones3d(:,3)= hydrophonedepths'; 

end

