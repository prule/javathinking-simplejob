package com.javathinking.batch.job;

/**
 * @author paul
 */
public abstract class AbstractTask implements Task {
    private String discriminator;
    private String name;

    public AbstractTask(String discriminator, String name) {
        this.discriminator = discriminator;
        this.name = name;
    }

    public String getDiscriminator() {
        return discriminator;
    }

    public String getName() {
        return name;
    }


}
