-- Users table
CREATE TABLE users (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       email VARCHAR(255) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       first_name VARCHAR(50) NOT NULL,
                       last_name VARCHAR(50) NOT NULL,
                       phone_number VARCHAR(20),
                       role ENUM('GUEST', 'HOST', 'ADMIN') NOT NULL,
                       profile_picture_url VARCHAR(255),
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Admins table (inherits from users)
CREATE TABLE admins (
                        user_id BIGINT PRIMARY KEY,
                        FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Hosts table (inherits from users)
CREATE TABLE hosts (
                       user_id BIGINT PRIMARY KEY,
                       bio TEXT,
                       super_host BOOLEAN DEFAULT FALSE,
                       FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Guests table (inherits from users)
CREATE TABLE guests (
                        user_id BIGINT PRIMARY KEY,
                        FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Properties table
CREATE TABLE properties (
                            id BIGINT AUTO_INCREMENT PRIMARY KEY,
                            host_id BIGINT NOT NULL,
                            title VARCHAR(255) NOT NULL,
                            description TEXT,
                            price_per_night DECIMAL(10, 2) NOT NULL,
                            max_guests INT NOT NULL,
                            bedrooms INT NOT NULL,
                            beds INT NOT NULL,
                            bathrooms INT NOT NULL,
                            property_type ENUM('APARTMENT', 'HOUSE', 'VILLA', 'COTTAGE', 'CHALET', 'BUNGALOW', 'CABIN', 'STUDIO') NOT NULL,
                            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                            FOREIGN KEY (host_id) REFERENCES hosts(user_id)
);

-- Locations table (embedded in properties)
CREATE TABLE locations (
                           property_id BIGINT PRIMARY KEY,
                           country VARCHAR(100) NOT NULL,
                           city VARCHAR(100) NOT NULL,
                           address VARCHAR(255) NOT NULL,
                           postal_code VARCHAR(20) NOT NULL,
                           latitude DECIMAL(10, 8),
                           longitude DECIMAL(11, 8),
                           FOREIGN KEY (property_id) REFERENCES properties(id)
);

-- Amenities table
CREATE TABLE amenities (
                           id BIGINT AUTO_INCREMENT PRIMARY KEY,
                           name VARCHAR(100) NOT NULL UNIQUE,
                           icon VARCHAR(255)
);

-- Property_Amenities junction table
CREATE TABLE property_amenities (
                                    property_id BIGINT,
                                    amenity_id BIGINT,
                                    PRIMARY KEY (property_id, amenity_id),
                                    FOREIGN KEY (property_id) REFERENCES properties(id),
                                    FOREIGN KEY (amenity_id) REFERENCES amenities(id)
);

-- Bookings table
CREATE TABLE bookings (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          property_id BIGINT NOT NULL,
                          guest_id BIGINT NOT NULL,
                          check_in_date DATE NOT NULL,
                          check_out_date DATE NOT NULL,
                          status ENUM('PENDING', 'CONFIRMED', 'CANCELED', 'COMPLETED') NOT NULL,
                          total_price DECIMAL(10, 2) NOT NULL,
                          number_of_guests INT NOT NULL,
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                          FOREIGN KEY (property_id) REFERENCES properties(id),
                          FOREIGN KEY (guest_id) REFERENCES guests(user_id)
);

-- Reviews table
CREATE TABLE reviews (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         property_id BIGINT NOT NULL,
                         guest_id BIGINT NOT NULL,
                         host_id BIGINT NOT NULL,
                         rating INT NOT NULL,
                         comment TEXT,
                         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                         FOREIGN KEY (property_id) REFERENCES properties(id),
                         FOREIGN KEY (guest_id) REFERENCES guests(user_id),
                         FOREIGN KEY (host_id) REFERENCES hosts(user_id)
);

-- Photos table
CREATE TABLE photos (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        property_id BIGINT NOT NULL,
                        url VARCHAR(255) NOT NULL,
                        caption VARCHAR(255),
                        is_primary BOOLEAN DEFAULT FALSE,
                        uploaded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        FOREIGN KEY (property_id) REFERENCES properties(id)
);

-- Favorites table
CREATE TABLE favorites (
                           id BIGINT AUTO_INCREMENT PRIMARY KEY,
                           guest_id BIGINT NOT NULL,
                           property_id BIGINT NOT NULL,
                           favorited_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                           FOREIGN KEY (guest_id) REFERENCES guests(user_id),
                           FOREIGN KEY (property_id) REFERENCES properties(id),
                           UNIQUE KEY (guest_id, property_id)
);