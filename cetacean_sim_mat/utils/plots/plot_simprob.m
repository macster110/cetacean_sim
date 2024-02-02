function [] = plot_simprob(psurf)
%PLOT_SIMPROB Plots the results of a simulation 

    hold on
%      b = surf(X, Y, hist_mean', 'FaceAlpha', 0.4);
    b = surf(psurf.rangeq,psurf.depthq, psurf.pq, 'FaceAlpha', 0.9);
    b.EdgeColor = 'none';
    
    [cont, h]=contour(psurf.rangeq,psurf.depthq, psurf.pq, 'LineWidth', 2);
%     plot3(prob_contour(1,:), prob_contour(2,:), 2*ones(length(prob_contour(2,:)),1), 'Color', [ 0.8500    0.3250    0.0980] , 'LineWidth',2 )
    
%     ylim([-180, 0])
%     xlim([0, 750])
   c= colorbar;
    xlabel('Range (m)')
    zlabel('n')
    ylabel ('Depth (m)')
    ylabel (c,'$$\hat{P}$$','Interpreter','Latex');
    
    hold off

end

