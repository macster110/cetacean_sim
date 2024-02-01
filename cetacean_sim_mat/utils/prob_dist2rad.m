function [probdistrad] = prob_dist2rad(probdist)
%PROB_DIST2RAD Converts the values in a probability distirbution structure
%from degrees to rad. 

probdistrad.type=probdist.type;

%normal
probdistrad.mean=deg2rad(probdist.mean);
probdistrad.std=deg2rad(probdist.std);

%log-normal
%erm whole log thing is a bit weird with this but will convert anyway 
probdistrad.scale=deg2rad(probdist.scale); 
probdistrad.shape=deg2rad(probdist.shape);
probdistrad.truncation=probdist.truncation;
probdistrad.flipnegative=probdist.flipnegative;

%uniform and custom
probdistrad.min=deg2rad(probdist.min);
probdistrad.max=deg2rad(probdist.max);

probdistrad.customp=probdist.customp;
probdistrad.limits=probdist.limits;


end

