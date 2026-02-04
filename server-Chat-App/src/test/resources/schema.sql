CREATE TABLE IF NOT EXISTS `users` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `phoneNumber` VARCHAR(20) NOT NULL UNIQUE,
    `name` VARCHAR(255) NOT NULL,
    `email` VARCHAR(255) NOT NULL UNIQUE,
    `picturePath` VARCHAR(500) NULL,
    `password` VARCHAR(255) NOT NULL,
    `role` ENUM('USER', 'ADMIN', 'MASTER_ADMIN') NOT NULL DEFAULT 'USER',
    `isFirstLogin` BOOLEAN NOT NULL DEFAULT TRUE,
    `gender` ENUM('MALE', 'FEMALE') NOT NULL,
    `country` VARCHAR(100) NOT NULL,
    `DOB` DATE NOT NULL,
    `bio` TEXT NULL,
    `status` ENUM('ONLINE','OFFLINE','AVAILABLE','BUSY','AWAY') NOT NULL DEFAULT 'OFFLINE',
    `lastSeen` TIMESTAMP NULL
);

CREATE TABLE IF NOT EXISTS `Room` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `type` ENUM('ONE_TO_ONE','GROUP') NOT NULL,
    `name` VARCHAR(255) NULL,
    `description` TEXT NULL,
    `picturePath` VARCHAR(500) NULL,
    `createdAt` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
    `lastMessageAt` TIMESTAMP NULL,
    `createdBy` BIGINT NULL,
    FOREIGN KEY (`createdBy`) REFERENCES `users` (`id`)
);

CREATE TABLE IF NOT EXISTS `Friends` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `senderUserId` BIGINT NOT NULL,
    `receiverUserId` BIGINT NOT NULL,
    `requestDate` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
    `responseDate` TIMESTAMP NULL,
    `status` ENUM('PENDING','ACCEPTED','REJECTED','BLOCKED') NOT NULL DEFAULT 'PENDING',
    FOREIGN KEY (`senderUserId`) REFERENCES `users` (`id`),
    FOREIGN KEY (`receiverUserId`) REFERENCES `users` (`id`)
);

CREATE TABLE IF NOT EXISTS `UserRooms` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `userId` BIGINT NOT NULL,
    `roomId` BIGINT NOT NULL,
    `isAdmin` BOOLEAN NOT NULL DEFAULT FALSE,
    `joinedAt` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
    `leftAt` TIMESTAMP NULL,
    `isActive` BOOLEAN NOT NULL DEFAULT TRUE,
    UNIQUE (`userId`, `roomId`),
    FOREIGN KEY (`userId`) REFERENCES `users` (`id`),
    FOREIGN KEY (`roomId`) REFERENCES `Room` (`id`)
);

CREATE TABLE IF NOT EXISTS `Message` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `senderId` BIGINT NOT NULL,
    `roomId` BIGINT NOT NULL,
    `text` TEXT NULL,
    `fontFamily` VARCHAR(50) DEFAULT 'Arial',
    `fontSize` INT DEFAULT 14,
    `fontColor` VARCHAR(7) DEFAULT '#000000',
    `backgroundColor` VARCHAR(7) NULL,
    `isBold` BOOLEAN NOT NULL DEFAULT FALSE,
    `isItalic` BOOLEAN NOT NULL DEFAULT FALSE,
    `isUnderline` BOOLEAN NOT NULL DEFAULT FALSE,
    `attachedFile` VARCHAR(500) NULL,
    `fileName` VARCHAR(255) NULL,
    `fileType` VARCHAR(50) NULL,
    `fileSize` BIGINT NULL,
    `sentAt` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
    `isDeleted` BOOLEAN NOT NULL DEFAULT FALSE,
    FOREIGN KEY (`senderId`) REFERENCES `users` (`id`),
    FOREIGN KEY (`roomId`) REFERENCES `Room` (`id`)
);

CREATE TABLE IF NOT EXISTS `Notification` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `receiverId` BIGINT NOT NULL,
    `type` ENUM('FRIEND_REQUEST','MESSAGE') NOT NULL,
    `content` TEXT NOT NULL,
    `friendId` BIGINT NULL,
    `createdAt` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
    `status` ENUM('UNREAD','READ','DELETED') NOT NULL DEFAULT 'UNREAD',
    `roomId` BIGINT NULL,
    FOREIGN KEY (`receiverId`) REFERENCES `users` (`id`),
    FOREIGN KEY (`roomId`) REFERENCES `Room` (`id`),
    FOREIGN KEY (`friendId`) REFERENCES `Friends` (`id`)
);

CREATE TABLE IF NOT EXISTS `ServerAnnouncement` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `title` VARCHAR(255) NOT NULL,
    `content` TEXT NOT NULL,
    `createdBy` VARCHAR(100) NOT NULL DEFAULT 'SYSTEM',
    `createdAt` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
    `expiresAt` TIMESTAMP NULL,
    `isActive` BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS `MessageStatus` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `messageId` BIGINT NOT NULL,
    `userId` BIGINT NOT NULL,
    `seenAt` TIMESTAMP NULL,
    UNIQUE (`messageId`, `userId`),
    FOREIGN KEY (`userId`) REFERENCES `users` (`id`),
    FOREIGN KEY (`messageId`) REFERENCES `Message` (`id`)
);