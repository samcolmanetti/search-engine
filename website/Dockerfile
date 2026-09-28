# PHP + Apache serving website/public. The official image already includes the curl extension.
FROM php:8.4-apache
ENV APACHE_DOCUMENT_ROOT=/var/www/html/public
RUN sed -ri 's!/var/www/html!${APACHE_DOCUMENT_ROOT}!g' /etc/apache2/sites-available/*.conf
COPY . /var/www/html/
