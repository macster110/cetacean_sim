function [cols] = getdefaultcols()
%GETDEFAULTCOLS Get the default matlab plot colours 
%   [COLS] = GETDEFAULTCOLS() returns a list of the default plot colours
%   for the current MATLAB version. 

cols=get(groot,'DefaultAxesColorOrder'); % HACk to get default colours 

end

