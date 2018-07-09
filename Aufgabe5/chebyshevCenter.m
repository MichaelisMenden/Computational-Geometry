function [xC,yC,rMax] = chebyshevCenter(A, b)
    %----
    % Compute Chebyshev center of polygon/polytope Ax <= b.
    %-----
    delta = A;

    %----
    % Optimization by maximizing r
    %-----

    % CVX Model is used for optimization, because of clearer syntax
    cvx_begin
        variable r(1)
        variable x(1)
        variable y(1)
        maximize ( r )

        % chebyshev center constraint: 
        % (yj?yi)x + (xj?xi)y + r*?[(xj?xi)^2+(yj?yi)^2] ? (xj?xi)yi - (yj?yi)xi 
        (delta(:,1)*y - delta(:,2)*x) + (r * sqrt(delta(:,1).^2 + delta(:,2).^2)) <= b;
    cvx_end

    xC = x;
    yC = y;
    rMax = r;
end
