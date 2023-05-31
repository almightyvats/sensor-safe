package com.almightyvats.sensorsafe.core;

import com.almightyvats.sensorsafe.model.custom.SanityCheckType;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.CreateCollectionOptions;
import com.mongodb.client.model.TimeSeriesOptions;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

/**
 * This class is responsible for managing the connection to the MongoDB database.
 * It is a singleton class, so only one instance of this class can exist at a time.
 * The class is a substitute for ReadingRepository, since it is not possible to
 * use Spring Data with MongoDB's Time Series Collections.
 */
@Component
public class TsDbManager {

    private final MongoDatabase database;

    private final String databaseName;

    private final String collectionName;

    public TsDbManager(@Value("${app.mongo.database}") String databaseName,
                       @Value("${app.mongo.ts.collection}") String collectionName,
                       @Value("${app.mongo.uri}") String mongoUri) {
        this.collectionName = collectionName;
        this.databaseName = databaseName;
        MongoClient mongoClient = MongoClients.create(mongoUri);
        CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));

        database = mongoClient.getDatabase(databaseName).withCodecRegistry(pojoCodecRegistry);
        TimeSeriesOptions tsOptions = new TimeSeriesOptions("timestamp");
        CreateCollectionOptions collOptions = new CreateCollectionOptions().timeSeriesOptions(tsOptions);
        if (!database.listCollectionNames().into(new ArrayList<>()).contains(collectionName)) {
            database.createCollection(collectionName, collOptions);
        }
    }

    /**
     * Returns the database name
     */
    public String getDatabaseName() {
        return databaseName;
    }

    /**
     * Returns the collection name of the database.
     */
    public String getCollectionName() {
        return collectionName;
    }

    /**
     * Inserts a new document into the database
     *
     * @param newReading the document to be inserted
     */
    public void insert(@NotNull Document newReading) {
        database.getCollection(collectionName).insertOne(newReading);
    }

    /**
     * Inserts a list of documents into the database
     *
     * @param newReadings the list of documents to be inserted
     */
    public void insert(@NotNull List<Document> newReadings) {
        database.getCollection(collectionName).insertMany(newReadings);
    }

    /**
     * Returns a list of documents from the database by a given sensor id
     *
     * @param uniqueHardwareName the uniqueHardwareName of the sensor
     * @return List<Document>
     */
    public List<Document> getReadingsBySensor(String uniqueHardwareName) {
        return database.getCollection(collectionName)
                .find(new Document("uniqueHardwareName", uniqueHardwareName)).into(new ArrayList<>());
    }

    /**
     * Returns a list of documents from the database by a given sensor name and time range
     *
     * @param uniqueHardwareName the uniqueHardwareName of the sensor
     * @param from the start time of the time range
     * @param to the end time of the time range
     */
    public List<Document> getReadingsBySensorAndTimeRange(String uniqueHardwareName, Date from, Date to) {
        return database.getCollection(collectionName)
                .find(new Document("uniqueHardwareName", uniqueHardwareName)
                        .append("timestamp", new Document("$gte", from).append("$lte", to)))
                .into(new ArrayList<>());
    }

    /**
     * Check if a document with certain properties exists in the database
     *
     * @param query the query to be checked
     * @return boolean
     */
    public boolean checkIfDocumentExists(Document query) {
        return database.getCollection(collectionName)
                .find(query)
                .into(new ArrayList<>()).size() > 0;
    }

    /**
     * Drops the collection from the database
     */
    public void dropCollection() {
        database.getCollection(collectionName).drop();
    }

    /**
     * Drops the database
     */
    public void dropDatabase() {
        database.drop();
    }

    /**
     * Returns the number of documents in the database
     *
     * @return long
     */
    public long getNumberOfDocuments() {
        return database.getCollection(collectionName).countDocuments();
    }

    /**
     * Returns the number of documents in the database
     * by a given sensor id
     *
     * @param uniqueHardwareName the uniqueHardwareName of a sensor
     * @return long
     */
    public long getNumberOfDocumentsBySensor(String uniqueHardwareName) {
        return database.getCollection(collectionName)
                .countDocuments(new Document("uniqueHardwareName", uniqueHardwareName));
    }

    /**
     * Deletes all documents from the collection
     */
    public void deleteAllDocuments() {
        database.getCollection(collectionName).deleteMany(new Document());
    }

    /**
     * Returns a count of sanity check type for the given sensor name.
     *
     * @param uniqueHardwareName the uniqueHardwareName of a sensor
     * @param sanityCheckType the type of sanity check
     * @return int
     */
    public int getSanityCheckTypeCountBySensor(String uniqueHardwareName, SanityCheckType sanityCheckType) {
        return (int) database.getCollection(collectionName)
                .countDocuments(new Document("uniqueHardwareName", uniqueHardwareName)
                        .append("sanityFlag", new Document("$in", Collections.singletonList(sanityCheckType.toString()))));
    }
}

