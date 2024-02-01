function [probstruct] = prob_dist(type,var, limits)
%PROB_DIST Creates a probabilit distirbution object. This compatible with
%JAVA and can be passed between MATLAB and CetSim.
%   [PROBSTRUCT]=PROB_DIST(TYPE,VAR) creates a probability distirbution
%   object. TYPE is a string of the distirbution type. This can be
%   'normal', lognormal', uniform','custom'
%
%   [PROBSTRUCT]=PROB_DIST('NORMAL' [MEAN, STD]) will produce a normal
%   distribution structure.
%
%   [PROBSTRUCT]=PROB_DIST('LOG-NORMAL', [SHAPE, SCALE, TRUNCATION
%   FLIPNEGATIVE]) will produce a log normal dist structure with a
%   specified SHAPE and SCALE and a boolean FLIPNEGATIVE.
%
%   [PROBSTRUCT]=PROB_DIST('UNIFORM' [MIN, MAX]) will produce a uniforma
%   dist structure between limits of MIN and MAX
%
%   [PROBSTRUCT]=PROB_DIST('CUSTOM' {MIN, MAX, CUSTOMP}) will produce a
%   custom distribution structure between limits MIN and MAX with a custom
%   distribution CUSTOMP defining the uniformly spaced P probabilities
%   between MIN and MAX.

if nargin<3
   limits=[-10000, 0]; 
end
%% set up a structure
% the type of distirbution.
probstruct.type=type;

% pre allocate so that all the strcuts can be kep in the same array if 
%necessary 

%normal
probstruct.mean=0;
probstruct.std=20;

%log-normal
probstruct.scale=1.0;
probstruct.shape=3.0;
probstruct.truncation=100.0;
probstruct.flipnegative=false;

%uniform and custom
probstruct.min=0;
probstruct.max=100;

%custom
probstruct.customp=[];

% limits to use in the distribution
probstruct.limits=limits;

%% now create the distribution
%normal distribution
if strcmpi(type,'Normal')
    probstruct.mean=var(1);
    probstruct.std=var(2);
end
%log-normal distribution
if strcmpi(type,'Log-normal')
    probstruct.scale=var(1);
    probstruct.shape=var(2);
    probstruct.truncation=var(3);
    probstruct.flipnegative=var(4);
end
%uniform distribution
if strcmpi(type,'Uniform')
    probstruct.min=var(1);
    probstruct.max=var(2);
end
%custom distirbution
if strcmpi(type,'Custom')
    probstruct.min=var{1};
    probstruct.max=var{2};
    probstruct.customp=var{3};
end

end

