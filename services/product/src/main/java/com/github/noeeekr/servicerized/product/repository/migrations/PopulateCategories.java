package com.github.noeeekr.servicerized.product.repository.migrations;

import com.github.noeeekr.servicerized.product.repository.models.CategoryEntity;
import com.github.noeeekr.servicerized.product.repository.models.Models;
import liquibase.change.custom.CustomSqlChange;
import liquibase.change.custom.CustomSqlRollback;
import liquibase.database.Database;
import liquibase.exception.CustomChangeException;
import liquibase.exception.RollbackImpossibleException;
import liquibase.exception.SetupException;
import liquibase.exception.ValidationErrors;
import liquibase.resource.ResourceAccessor;
import liquibase.statement.SqlStatement;
import liquibase.statement.core.RawSqlStatement;

public class PopulateCategories implements CustomSqlChange, CustomSqlRollback {
    private static final String TABLE_NAME =
            Models.SCHEMA + "." + CategoryEntity.METADATA.TABLE_NAME;

    @Override
    public void setUp() throws SetupException {
        return;
    }

    @Override
    public ValidationErrors validate(Database database) {
        return null;
    }

    @Override
    public void setFileOpener(ResourceAccessor resourceAccessor) {
        return;
    };


    @Override
    public String getConfirmationMessage() {
        return String.format("System pre-populating schema '%s' table '%s' with default rows.",
                Models.SCHEMA, CategoryEntity.METADATA.TABLE_NAME);
    }

    @Override
    public SqlStatement[] generateStatements(Database database) throws CustomChangeException {
        /**
         * AI generated list of generic service categories. Prompt: "names for service people can do as comissions" to Gemini 3.5 Flash-Lite.
         **/
        String[] categoriesNames = {"Digital Art", "Character Design", "Concept Art",
                "Anime Drawing", "Portrait Illustration", "Fan Art Commission", "Comic Strip",
                "3D Modeling", "3D Animation", "Live2D Rigging", "Logo Design", "Banner Design",
                "Emote Design", "Stream Overlay Design", "Video Editing", "Gaming Montage",
                "Shorts/TikTok Editing", "Audio Mixing", "Voice Acting", "Custom Narration",
                "Songwriting", "Custom Instrumental", "Lyric Writing", "Singing/Vocals",
                "Language Lessons", "Music Lessons", "Singing Lessons", "Coding Tutoring",
                "Academic Tutoring", "Test Preparation", "Gaming Coaching",
                "Playing Together (Gaming)", "Gaming Buddy", "Virtual Companion",
                "Talking & Venting Session", "Accountability Partner", "Life Coaching",
                "Tarot Reading", "Astrology Reading", "Personalized Horoscope",
                "Gaming Strategy Guide", "Custom Walkthrough", "Recipe Creation", "Meal Planning",
                "Fitness Coaching", "Workout Plan", "Fashion Consultation", "Makeup Tutorial",
                "Interior Design Advice", "Creative Writing", "Poetry Writing",
                "Fanfiction Writing", "Storytelling", "Proofreading", "Translation Services",
                "Resume & CV Review", "Scriptwriting", "Copywriting", "Social Media Management",
                "Custom Playlist", "Personalized Video Message", "Video Shoutout",
                "Virtual Tour Guide", "Travel Itinerary Planning", "Modding Assistance",
                "Server Setup & Configuration", "Tech Support", "Discord Server Setup",
                "Bot Configuration", "Website Review", "UI/UX Feedback", "Stream Moderation",
                "Community Management", "Market Research", "Survey Participation",
                "Product Testing", "Beta Testing", "Data Entry", "Virtual Assistance",
                "Transcription", "Photo Retouching", "Color Grading", "Meme Creation",
                "Custom Craft Patterns", "Knitting/Crochet Guides", "Cosplay Props & Tutorials",
                "Pet Care Advice", "Plant Care Consultation", "Feng Shui Advice",
                "Mindfulness Coaching", "Meditation Guidance", "Dream Interpretation",
                "Handwriting & Calligraphy", "Custom Embroidery Design", "Digital Scrapbooking",
                "Virtual Event Hosting", "Trivia Hosting", "D&D Campaign DMing",
                "Tabletop RPG Session", "General Consultation"};

        StringBuilder valuesStatementBuilder = new StringBuilder();
        for (int i = 0; i < categoriesNames.length; i++) {
            valuesStatementBuilder.append(String.format("(%s)", categoriesNames[i]));
            if (i != categoriesNames.length - 1) {
                valuesStatementBuilder.append(", ");
            }
        }

        String fieldStatement = String.format("(%s.%s)", TABLE_NAME,
                CategoryEntity.METADATA.DATABASE_COLUMN_NAME_CATEGORY_NAME);

        String statement = String.format("INSERT INTO %s %s VALUES %s", TABLE_NAME,
                valuesStatementBuilder.toString(), fieldStatement);
        return new SqlStatement[] {new RawSqlStatement(statement)};
    }

    @Override
    public SqlStatement[] generateRollbackStatements(Database database)
            throws CustomChangeException, RollbackImpossibleException {
        // Unnecessary: migrations table drop statement handles the rollback & no imaginable use
        // case for dropping the default fields while live.
        return new SqlStatement[] {};
    }
}
